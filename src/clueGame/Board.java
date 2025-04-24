package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

public class Board extends JPanel {
	/*
	 * variable and methods used for singleton pattern
	 */
	private BoardCell[][] grid; // 2D array holds board grid
	private int numRows; // Number of rows on the board
	private int numColumns; // Number of columns on the board
	private String layoutConfigFile; //layout configuration file
	private String setupConfigFile; // setup configuration file
	private Map<Character, Room> roomMap; // Map between character and room
	private Set<BoardCell> targets; // possible target cells
	private List<Player> players = new ArrayList<>(); // stores players in a list
	private List<String> weapons = new ArrayList<>(); // stores weapons in a list
	private List<Card> deck = new ArrayList<>(); // stores all game cards
	private List<Card> solution; // stores the solution cards
	private Solution theAnswer; // stores the answer object
	private Random random = new Random(System.nanoTime());
	private GameControlPanel controlPanel;
	private boolean unfinished = true;
	Player currentPlayer = null;
	private int playerIndex = 0;

	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super();
		roomMap = new HashMap<>(); // initializes the room map
	}

	/**
	 * Returns the instance of the game board from singleton pattern
	 * @return The instance of the game board. (There is only one instance of Board).
	 */
	public static Board getInstance() {
		return theInstance;
	}

	/**
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() {
		theAnswer = null;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int cellWidth = getWidth() / numColumns;
				int cellHeight = getHeight() / numRows;

				int clickedColumn = e.getX() / cellWidth;
				int clickedRow = e.getY() / cellHeight;

				handleMouseClick(clickedRow, clickedColumn);
			}
		});
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col] = new BoardCell(row, col, 'X');
			}
		}
		try {
			clearData();
			loadSetupConfig();
			loadLayoutConfig();
			deal();
		} catch (BadConfigFormatException e) {
			System.err.println("Bad Config Format");
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Method to clear data if needed
	 */
	public void clearData() {
		players.clear();
		weapons.clear();
		deck.clear();
		theAnswer = null;
	}
	/**
	 * Loads txt setup config file with space and room information
	 * @throws BadConfigFormatException if file format is incorrect
	 */
	public void loadSetupConfig() throws BadConfigFormatException {
		try {
			File file = new File(setupConfigFile);
			Scanner input = new Scanner(file);
			while (input.hasNextLine()) {
				String line = input.nextLine();	            

				ArrayList<String> tokens = tokenize(line, ", ");

				if (line.substring(0,2).equals("//")) {
					continue;
				}

				String type = tokens.get(0);

				if (!type.equals("Room") && !type.equals("Space") && !type.equals("Player") && !type.equals("Weapon")) {
					throw new BadConfigFormatException("Invalid type " + type);
				}

				if(type.equals("Room") || type.equals("Space")) {
					String name = tokens.get(1);
					char character = tokens.get(2).charAt(0);
					roomMap.put(character, new Room(name));
					if(type.equals("Room")) {
						deck.add(new Card(name, CardType.ROOM));
					}
				}

				if(type.equals("Player")) {
					String playerName = tokens.get(1);
					String playerColor = tokens.get(2);
					String playerType = tokens.get(3);
					int playerRow = Integer.parseInt(tokens.get(4));
					int playerCol = Integer.parseInt(tokens.get(5));

					if(playerType.equals("HUMAN")) {
						players.add(new HumanPlayer(playerName, playerColor, playerRow, playerCol));
					} else {
						players.add(new ComputerPlayer(playerName, playerColor, playerRow, playerCol));
					}
					deck.add(new Card(playerName, CardType.PERSON));
				}

				if(type.equals("Weapon")) {
					weapons.add(tokens.get(1));
					deck.add(new Card(tokens.get(1), CardType.WEAPON));
				}

			}
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("Setup config file not found.");
		}
	}

	/**
	 * Loads the Layout Config file which contains the board layout.
	 * @throws BadConfigFormatException if layout is incorrect
	 */
	public void loadLayoutConfig() throws BadConfigFormatException {
		try {
			File layout = new File(layoutConfigFile);

			// Read the dimensions of the file. Any size board should be readable as long as the board is a square.
			Scanner dimensionReader = new Scanner(layout);
			numRows = 0;
			numColumns = 0;

			while (dimensionReader.hasNextLine()) {
				String line = dimensionReader.nextLine();
				if (!line.trim().isEmpty()) {
					ArrayList<String> tokens = tokenize(line, ",");
					numColumns = Math.max(numColumns, tokens.size());
					numRows++;
				}
			}
			dimensionReader.close();

			grid = new BoardCell[numRows][numColumns];

			try (Scanner reader = new Scanner(layout, "UTF-8")) {
				int row = 0;
				// add each value to grid with properties
				while (reader.hasNextLine()) {
					ArrayList<String> line = tokenize(reader.nextLine(), ",");
					if (!line.isEmpty()) {
						for (int col = 0; col < line.size(); col++) {
							if (line.get(col).isEmpty()) {
								throw new BadConfigFormatException("Empty column at: " + row + ", " + col);
							}

							// Handle potential BOM character
							String cellContent = line.get(col);
							char roomInitial = cellContent.charAt(0);
							if (roomInitial == '\uFEFF' && cellContent.length() > 1) {
								roomInitial = cellContent.charAt(1);
							}

							// only run if initial doesn't match and not BOM char
							if (!roomMap.containsKey(roomInitial) && roomInitial != 65279) {
								throw new BadConfigFormatException("Initial doesn't exist at: " + row + ", " + col);
							}

							BoardCell temp = new BoardCell(row, col, roomInitial);

							// check if position has additional character indicating special values
							if (cellContent.length() > 1) {
								char specialChar = cellContent.charAt(0) == '\uFEFF' && cellContent.length() > 2
										? cellContent.charAt(2)
												: (cellContent.length() > 1 ? cellContent.charAt(1) : roomInitial);

								switch (specialChar) {
								case ('<'):
									temp.setDoorDirection(DoorDirection.LEFT);
								break;
								case ('>'):
									temp.setDoorDirection(DoorDirection.RIGHT);
								break;
								case ('^'):
									temp.setDoorDirection(DoorDirection.UP);
								break;
								case ('v'):
									temp.setDoorDirection(DoorDirection.DOWN);
								break;
								case ('#'):
									temp.setRoomLabel(true);
								roomMap.get(roomInitial).setLabelCell(temp);
								break;
								case ('*'):
									temp.setRoomCenter(true);
								roomMap.get(roomInitial).setCenterCell(temp);
								break;
								default:
									// If length > 1 and no other cases occur, this cell must be a secret passage
									if (specialChar != roomInitial) {
										temp.setSecretPassage(specialChar);
									}
									break;
								}
							}

							// put cell into grid
							try {
								grid[row][col] = temp;
							} catch (Exception e) {
								System.err.println("Error: Grid not initialized properly");
								return;
							}
						}
					}
					row++;
				}
				calcAdj();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Layout config file not found.");
		}
	}

	/**
	 *
	 * @param str String to tokenize
	 * @param token Token to split each String
	 * @return - ArrayList of all the strings after being split up by the token
	 */
	public ArrayList<String> tokenize(String str, String token) {
		ArrayList<String> result = new ArrayList<>();

		if (str == null || str.isEmpty()) {
			return result;
		}

		int start = 0;
		int tokenLength = token.length();

		while (true) {
			int idx = str.indexOf(token, start);

			if (idx == -1) {
				result.add(str.substring(start));
				break;
			}

			result.add(str.substring(start, idx));
			start = idx + tokenLength;
		}

		return result;
	}

	/**
	 * Calculate the adjacencies for each cell on the board.
	 */
	public void calcAdj() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				if (cell.hasSecretPassage()) {
					BoardCell roomCenter = getRoom(cell.getInitial()).getCenterCell();
					BoardCell secretCenter = getRoom(cell.getSecretPassage()).getCenterCell();
					roomCenter.addAdj(secretCenter);
				}
				if (cell.getInitial() == 'W') {
					if (row > 0 && grid[row - 1][col].getInitial() == 'W') cell.addAdj(grid[row - 1][col]);
					if (row < numRows - 1 && grid[row + 1][col].getInitial() == 'W') cell.addAdj(grid[row + 1][col]);
					if (col > 0 && grid[row][col - 1].getInitial() == 'W') cell.addAdj(grid[row][col - 1]);
					if (col < numColumns - 1 && grid[row][col + 1].getInitial() == 'W') cell.addAdj(grid[row][col + 1]);
					if (cell.isDoorway()) {
						BoardCell centerCell;
						switch (cell.getDoorDirection()) {
						case UP:
							cell.addAdj(getRoom(grid[row - 1][col].getInitial()).getCenterCell());
							centerCell = getRoom(grid[row - 1][col].getInitial()).getCenterCell();
							centerCell.addAdj(cell);
							break;
						case DOWN:
							cell.addAdj(getRoom(grid[row + 1][col].getInitial()).getCenterCell());
							centerCell = getRoom(grid[row + 1][col].getInitial()).getCenterCell();
							centerCell.addAdj(cell);
							break;
						case LEFT:
							cell.addAdj(getRoom(grid[row][col - 1].getInitial()).getCenterCell());
							centerCell = getRoom(grid[row][col - 1].getInitial()).getCenterCell();
							centerCell.addAdj(cell);
							break;
						case RIGHT:
							cell.addAdj(getRoom(grid[row][col + 1].getInitial()).getCenterCell());
							centerCell = getRoom(grid[row][col + 1].getInitial()).getCenterCell();
							centerCell.addAdj(cell);
							break;
						default:
							break;
						}
					}

				} else if (cell.isDoorway()) {
					BoardCell centerCell;
					switch (cell.getDoorDirection()) {
					case UP:
						cell.addAdj(getRoom(grid[row - 1][col].getInitial()).getCenterCell());
						centerCell = getRoom(grid[row - 1][col].getInitial()).getCenterCell();
						centerCell.addAdj(cell);
						break;
					case DOWN:
						cell.addAdj(getRoom(grid[row + 1][col].getInitial()).getCenterCell());
						centerCell = getRoom(grid[row + 1][col].getInitial()).getCenterCell();
						centerCell.addAdj(cell);
						break;
					case LEFT:
						cell.addAdj(getRoom(grid[row][col - 1].getInitial()).getCenterCell());
						centerCell = getRoom(grid[row][col - 1].getInitial()).getCenterCell();
						centerCell.addAdj(cell);
						break;
					case RIGHT:
						cell.addAdj(getRoom(grid[row][col + 1].getInitial()).getCenterCell());
						centerCell = getRoom(grid[row][col + 1].getInitial()).getCenterCell();
						centerCell.addAdj(cell);
						break;
					default:
						break;
					}
					if (row > 0 && grid[row - 1][col].getInitial() == 'W') cell.addAdj(grid[row - 1][col]);
					if (row < numRows - 1 && grid[row + 1][col].getInitial() == 'W') cell.addAdj(grid[row + 1][col]);
					if (col > 0 && grid[row][col - 1].getInitial() == 'W') cell.addAdj(grid[row][col - 1]);
					if (col < numColumns - 1 && grid[row][col + 1].getInitial() == 'W') cell.addAdj(grid[row][col + 1]);
				} else if (cell.getInitial() != 'X') {
					Set<BoardCell> adjList = getRoom(cell.getInitial()).getCenterCell().getAdjList();
					cell.setAdjList(adjList);
				}
			}
		}
	}

	/**
	 * Calculate the targets for a given cell and path length.
	 * @param startCell The cell to start from.
	 * @param pathlength The number of steps to take.
	 */
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets = new HashSet<>();
		HashSet<BoardCell> visited = new HashSet<>();
		visited.add(startCell);
		if (!startCell.isRoomCenter() && getRoom(startCell) != null) {
			BoardCell roomCenter = getRoom(startCell).getCenterCell();
			if (roomCenter != null) {
				visited.add(roomCenter);
			}
		}
		findAllTargets(startCell, pathlength, visited);
	}

	/**
	 * Recursive function to find all targets for a given cell and path length.
	 * @param cell The cell to start from.
	 * @param stepsRemaining The number of steps to take.
	 * @param visited The set of visited cells.
	 */
	private void findAllTargets(BoardCell cell, int stepsRemaining, HashSet<BoardCell> visited) {
		visited = new HashSet<>(visited);
		for (BoardCell adj : cell.getAdjList()) {
			if (visited.contains(adj) || (adj.getOccupied() && !adj.isRoomCenter())) {
				continue;
			}
			visited.add(adj);

			if (stepsRemaining == 1 || adj.isRoomCenter()) {
				targets.add(adj); // need to end on roll number
				continue;
			} else  {
				findAllTargets(adj, stepsRemaining - 1, visited); // repeat until room or 1 left
			}
			visited.remove(adj); // remove backtrack
		}
	}

	/**
	 * Set the files to use for configuration of the game board.
	 * @param layoutFile The name of the config file used for layout.
	 * @param setupFile The name of the config file used for setup.
	 */
	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = "data/" + layoutFile;
		setupConfigFile = "data/" + setupFile;
	}

	/**
	 * deal method for cards after setup
	 */
	public void deal() {
		List<Card> shuffledDeck = new ArrayList<>(deck);
		Collections.shuffle(shuffledDeck);

		solution = new ArrayList<>();

		Card room = null, weapon = null, person = null;

		Iterator<Card> iterator = shuffledDeck.iterator();
		while (iterator.hasNext() && (room == null || weapon == null || person == null)) {
			Card card = iterator.next();
			if (card.getCardType() == CardType.ROOM && room == null) {
				room = card;
				solution.add(room);
				iterator.remove();
			} else if (card.getCardType() == CardType.WEAPON && weapon == null) {
				weapon = card;
				solution.add(weapon);
				iterator.remove();
			} else if (card.getCardType() == CardType.PERSON && person == null) {
				person = card;
				solution.add(person);
				iterator.remove();
			}
		}

		if (solution.size() == 3) {
			theAnswer = new Solution(solution.get(0), solution.get(1), solution.get(2));
		}

		int playerCount = players.size();
		int playerIndex = 0;

		if (playerCount > 0) {
			for (Card card : shuffledDeck) {
				players.get(playerIndex).addCard(card);
				playerIndex = (playerIndex + 1) % playerCount;
			}
		}
	}

	/**
	 * Handles a suggestion made by a player.
	 * @param suggestion The suggestion made by the player.
	 * @param suggestingPlayer The player who made the suggestion.
	 * @return The card that disproves the suggestion, or null if no player can disprove it.
	 */
	public Card handleSuggestion(Solution suggestion, Player suggestingPlayer) {
		int playerIndex = players.indexOf(suggestingPlayer);

		for (int i = 1; i < players.size(); i++) {
			int index = (playerIndex + i) % players.size();
			Player currentPlayer = players.get(index);

			Card cardToDisprove = currentPlayer.disproveSuggestion(suggestion);
			if (cardToDisprove != null) {
				return cardToDisprove;
			}
		}
		return null;
	}

	/**
	 * Highlights the entire room on the board.
	 * @param roomInitial The initial of the room to highlight.
	 */
	private void highlightEntireRoom(char roomInitial) {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				if (cell.getInitial() == roomInitial) {
					cell.setHighlightRoom(true);
				}
			}
		}
		repaint();
	}

	/**
	 * Handles the turn for the current player.
	 */
	public void handleTurn() {
		int diceRoll = (int)(random.nextInt(6) + 1);
		
		currentPlayer = players.get(playerIndex);

		controlPanel.setTurn(currentPlayer, diceRoll);

		calcTargets(grid[currentPlayer.getRow()][currentPlayer.getColumn()], diceRoll);

		for (BoardCell[] row : grid) {
			for (BoardCell cell : row) {
				cell.setTarget(false);
				cell.setHighlightRoom(false);
			}
		}

		if (currentPlayer.getType().equals("HUMAN")) {
			for (BoardCell cell : targets) {
				unfinished = true;
				cell.setTarget(true);
				if (cell.isRoomCenter()) {
					char roomInitial = cell.getInitial();
					highlightEntireRoom(roomInitial);
				}
			}
			repaint();
		} else {
			makeAccusation(); // implement later
			
			BoardCell targetCell = currentPlayer.selectTarget(targets);
			currentPlayer.setPosition(targetCell.getRow(), targetCell.getCol());
			repaint();
			
			makeSuggestion(); // implement later
		}
	}

	/**
	 * Get the card associated with the given name.
	 * @param cardName - the name of the card being searched for.
	 * @return - the card with the given name.
	 */
	public static Card toCard(String cardName) {
		for(Card card : getInstance().getDeck()) {
			if (card.getCardName().equals(cardName)) {
				return card;
			}
		}
		return null;
	}

	/**
	 * Method will be implemented to handle the creation of making a suggestion
	 */
	private void makeSuggestion() {
		// TODO Auto-generated method stub

	}

	/**
	 * Method will be implemented to handle the creation of making an accusation
	 */
	public void makeAccusation() {
		// TODO Auto-generated method stub
	}

	/**
	 * Handles the next player's turn.
	 * @throws TurnNotFinishedException if the current player's turn is not finished.
	 */
	public void handleNextPlayer() throws TurnNotFinishedException {
		if (unfinished) {
			throw new TurnNotFinishedException("Turn not finished");
		}

		int playerCount = players.size();
		playerIndex = (playerIndex + 1) % playerCount;

		handleTurn();
	}

	/**
	 * Handles mouse click events on the board.
	 * @param clickedRow The row of the clicked cell.
	 * @param clickedColumn The column of the clicked cell.
	 */
	private void handleMouseClick(int clickedRow, int clickedColumn) {
		BoardCell clickedCell = grid[clickedRow][clickedColumn];
		if (currentPlayer instanceof HumanPlayer) {
			if (clickedCell.isTarget()) {
				currentPlayer.setPosition(clickedRow, clickedColumn);
				unfinished = false;
				for (BoardCell[] row : grid) {
					for (BoardCell cell : row) {
						cell.setTarget(false);
						cell.setHighlightRoom(false);
					}
				}
				repaint();
			} else if (getRoom(clickedCell.getInitial()).getCenterCell() != null && getRoom(clickedCell.getInitial()).getCenterCell().isTarget()) {
				char roomInitial = clickedCell.getInitial();
				BoardCell roomCenter = getRoom(roomInitial).getCenterCell();
				currentPlayer.setPosition(roomCenter.getRow(), roomCenter.getCol());
				unfinished = false;
				for (BoardCell[] row : grid) {
					for (BoardCell cell : row) {
						cell.setTarget(false);
						cell.setHighlightRoom(false);
					}
				}
				repaint();
				
				SuggestionPanel suggestion = new SuggestionPanel(getRoom(roomInitial));
		        suggestion.setVisible(true);
			} else {
				new SplashScreen("Invalid cell clicked", "Error").showSplash();
			}
		}
	}

	/**
	 * Paints all the components on the board.
	 * @param graphics the <code>Graphics</code> object to protect
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		int cellWidth = getWidth() / numColumns;
		int cellHeight = getHeight() / numRows;
		int cellDimension = Math.min(cellWidth, cellHeight);

		// Display all of the cells
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col].draw(graphics, row, col, cellDimension);
			}
		}

		// Display all players
		for (Player p : players) {
			p.draw(graphics, cellDimension);
		}

		// Write names above rooms centered at label
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				if (cell.isLabel()) {
					String roomName = getRoom(cell.getInitial()).getName();
					int positionCol = col * cellDimension;
					int positionRow = row * cellDimension;

					FontMetrics metrics = graphics.getFontMetrics();
					int textWidth = metrics.stringWidth(roomName);
					int textHeight = metrics.getHeight();

					int textCol = positionCol + (cellDimension - textWidth) / 2;
					int textRow = positionRow + (cellDimension + textHeight / 2) / 2;

					graphics.setColor(Color.BLUE);
					graphics.drawString(roomName, textCol, textRow);
				}
			}
		}
	}

	public HumanPlayer getHumanPlayer() {
		for (Player player : players) {
			if (player instanceof HumanPlayer) {
				return (HumanPlayer) player;
			}
		}
		return null;
	}

	public void setPlayer(Player player) {
		players.add(player);
	}

	public Room getRoom(char c) {
		return roomMap.get(c);
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCell(int i, int j) {
		return grid[i][j];
	}

	// Used in test files
	public Room getRoom(BoardCell cell) {
		return getRoom(cell.getInitial());
	}

	// Used in test files
	public Set<BoardCell> getAdjList(int row, int col) {
		return grid[row][col].getAdjList();
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	// get list of players
	public List<Player> getPlayers() {
		return players;
	}

	// get list of weapons
	public List<String> getWeapons() {
		return weapons;
	}

	// get list of cards in deck
	public List<Card> getDeck() {
		//Collections.shuffle(deck); // breaks tests when shuffled again
		return deck;
	}

	// get answer to game
	public Solution getAnswer() {
		return theAnswer;
	}

	public List<Card> getSolution() {
		return solution;
	}

	public void setControlPanel(GameControlPanel panel) {
		controlPanel = panel;
	}

	public void handleAccusation(Solution accusation) {
		if (accusation.equals(theAnswer)) {
			new SplashScreen("You win!", "Congratulations").showSplash();
		} else {
			new SplashScreen("You lose!", "Game Over").showSplash();
		}

	}
}
