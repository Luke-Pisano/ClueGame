package clueGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

import experiment.TestBoardCell;

public class Board {
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
	
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super();
		roomMap = new HashMap<>(); // initalizes the room map
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
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col] = new BoardCell(row, col, 'X');
			}
		}
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			System.err.println(e.getMessage());
		}
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

				if(tokens.size() != 3) {
					continue;
				}

				String type = tokens.get(0);
	            String name = tokens.get(1);
	            char character = tokens.get(2).charAt(0);

	            if (!type.equals("Room") && !type.equals("Space")) {
	                throw new BadConfigFormatException("Invalid type " + type);
	            }

				if(type.equals("Room") || type.equals("Space")) {
					roomMap.put(character, new Room(name));
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
					if (line.size() > 0) {
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
				reader.close();
				calcAdj();
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
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

	public Room getRoom(BoardCell cell) {
		// TODO Is this needed? Can we just use the other getRoom method?
		return getRoom(cell.getInitial());
	}

	// Used in test files
	public Set<BoardCell> getAdjList(int row, int col) {
		return grid[row][col].getAdjList();
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}
}
