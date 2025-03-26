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
	private Set<BoardCell> visited; // previous tile visited

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
		// Setups grid based on the dimensions
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col] = new BoardCell(row, col, 'X'); // add BoardCell at each row / col
			}
		}
		try {
			loadSetupConfig(); // load setup config for rooms / spaces
			loadLayoutConfig(); // load layout config for the board
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
			File file = new File(setupConfigFile); // load setup config file
			Scanner input = new Scanner(file);
			while (input.hasNextLine()) {
				String line = input.nextLine();	            

				ArrayList<String> tokens = tokenize(line, ", "); // split up line by commas

				if(tokens.size() != 3) {
					continue; // skip lines without 3 tokens (values)
				}

				String type = tokens.get(0);
				String name = tokens.get(1);
				char character = tokens.get(2).charAt(0);

				// if setup config has a bad type value not space, or room
				if (!type.equals("Room") && !type.equals("Space")) {
					throw new BadConfigFormatException("Invalid type " + type);
				}

				// store room and space with inital in the map
				if(type.equals("Room") || type.equals("Space")) {
					roomMap.put(character, new Room(name));
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loads the Layout Config file which contains the board layout.
	 * @throws BadConfigFormatException if layout is incorrect
	 */
	public void loadLayoutConfig() throws BadConfigFormatException {
		try {
			File layout = new File(layoutConfigFile); // load layout config file

			// Read the dimensions of the file. Any size board should be readable as long as the board is a square.
			Scanner dimensionReader = new Scanner(layout);
			numRows = 0;
			numColumns = 0;

			while (dimensionReader.hasNextLine()) {
				String line = dimensionReader.nextLine();
				if (!line.trim().isEmpty()) {
					ArrayList<String> tokens = tokenize(line, ","); // split line up at commas
					numColumns = Math.max(numColumns, tokens.size());
					numRows++;
				}
			}
			dimensionReader.close();

			grid = new BoardCell[numRows][numColumns]; // create grid based on columns and rows

			try (Scanner reader = new Scanner(layout)) {
				int row = 0;
				// add each value to grid with properties
				while (reader.hasNextLine()) {
					ArrayList<String> line = tokenize(reader.nextLine(), ","); // split line at commas
					if (line.size() > 0) {
						for (int col = 0; col < line.size(); col++) {
							if (line.get(col).isEmpty()) {
								throw new BadConfigFormatException("Empty column at: " + row + ", " + col);
							}

							char roomInitial = line.get(col).charAt(0);

							// only run if inital doesn't match and not empty char
							if (!roomMap.containsKey(roomInitial) && roomInitial != 65279) {
								throw new BadConfigFormatException("Inital doesn't exist at: " + row + ", " + col);
							}

							// create boardCell temp for the current cell
							BoardCell temp = new BoardCell(row, col, line.get(col).charAt(0));

							// check if position has additional character indicating special values
							if (line.get(col).length() > 1) {
								switch (line.get(col).charAt(1)) {
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
								roomMap.get(line.get(col).charAt(0)).setLabelCell(temp); // Set room label
								break;
								case ('*'):
									temp.setRoomCenter(true);
								roomMap.get(line.get(col).charAt(0)).setCenterCell(temp); // Set room center
								break;
								default:
									// If length > 1 and no other cases occur, this cell must be a secret passage
									temp.setSecretPassage(line.get(col).charAt(1));
									break;
								}
							}

							// put cell into grid
							try {
								grid[row][col] = temp;
							} catch (Exception e) {
								// if cell is null
								System.err.println("Error: Grid not initialized properly");
								return;
							}
						}
					}
					row++;
				}
				reader.close();
				calcAdj(); // build adj list
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return;
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

		// null or empty string
		if (str == null || str.isEmpty()) {
			return result;
		}

		int start = 0;
		int tokenLength = token.length();

		while (true) {
			int idx = str.indexOf(token, start);

			// if not empty or trailing token
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
	 * Set the files to use for configuration of the game board.
	 * @param layoutFile The name of the config file used for layout.
	 * @param setupFile The name of the config file used for setup.
	 */
	public void setConfigFiles(String layoutFile, String setupFile) {
		// TODO Auto-generated method stub
		layoutConfigFile = "data/" + layoutFile;
		setupConfigFile = "data/" + setupFile;
	}

	/**
	 *
	 * @param c The label of the room.
	 * @return The room object that has the given label.
	 */
	public Room getRoom(char c) {
		// TODO Auto-generated method stub
		return roomMap.get(c);
	}

	/**
	 *
	 * @return The number of rows contained on the board.
	 */
	public int getNumRows() {
		// TODO Auto-generated method stub
		return numRows;
	}

	/**
	 *
	 * @return The number of columns contained on the board.
	 */
	public int getNumColumns() {
		// TODO Auto-generated method stub
		return numColumns;
	}

	/**
	 *
	 * @param i The row of the desired cell.
	 * @param j The column of the desired cell.
	 * @return
	 */
	public BoardCell getCell(int i, int j) {
		// TODO Auto-generated method stub
		return grid[i][j];
	}

	/**
	 *
	 * @param cell The cell to be identified.
	 * @return The room object that the cell is contained within.
	 */
	public Room getRoom(BoardCell cell) {
		// TODO Auto-generated method stub
		return getRoom(cell.getInitial());
	}

	// Used in test files
	public Set<BoardCell> getAdjList(int row, int col) {
		// TODO Auto-generated method stub
		return grid[row][col].getAdjList();
	}

	public Set<BoardCell> getTargets() {
		// TODO Auto-generated method stub
		return new HashSet<>();
	}

	public void calcAdj() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				BoardCell centerCell = grid[row][col];
				if (cell.getInitial() == 'W') {
					if (row > 0 && grid[row - 1][col].getInitial() == 'W') cell.addAdj(grid[row - 1][col]);
					if (row < numRows - 1 && grid[row + 1][col].getInitial() == 'W') cell.addAdj(grid[row + 1][col]);
					if (col > 0 && grid[row][col - 1].getInitial() == 'W') cell.addAdj(grid[row][col - 1]);
					if (col < numColumns - 1 && grid[row][col + 1].getInitial() == 'W') cell.addAdj(grid[row][col + 1]);
					if (cell.isDoorway()) {
						switch (cell.getDoorDirection()) {
						case UP:
							centerCell = getRoom(grid[row - 1][col].getInitial()).getCenterCell();
							cell.addAdj(centerCell);
							break;
						case DOWN:
							centerCell = getRoom(grid[row + 1][col].getInitial()).getCenterCell();
							cell.addAdj(centerCell);
							break;
						case LEFT:
							centerCell = getRoom(grid[row][col - 1].getInitial()).getCenterCell();
							cell.addAdj(centerCell);
							break;
						case RIGHT:
							centerCell = getRoom(grid[row][col + 1].getInitial()).getCenterCell();
							cell.addAdj(centerCell);
							break;
						default: 
							break;
						}
						if(!centerCell.getAdjList().contains(cell)) {
							centerCell.addAdj(cell);
						}
					}

				}  else if (cell.hasSecretPassage()) {
					cell.addAdj(getRoom(cell.getSecretPassage()).getCenterCell());
				} else {
					//cell.setAdjList(getRoom(cell.getInitial()).getCenterCell().getAdjList());
				}
			}
		}
	}

	public void calcTargets(BoardCell startCell, int pathlength) {
		targets = new HashSet<>();
		visited = new HashSet<>();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	// recursive algorithm to find all possible cells can end on
	private void findAllTargets(BoardCell cell, int stepsRemaining) {
		for (BoardCell adj : cell.getAdjList()) {
			// skip if visited or occupied
			if (visited.contains(adj)) {
				continue;
			}
			if (adj != null && adj.getOccupied()) {
				continue;
			}
			visited.add(adj);


			if (stepsRemaining == 1) {
				targets.add(adj); // need to end on roll number
			} else if (adj.isDoorway()){
				targets.add(adj); // room don't need to use full roll
			} else {
				findAllTargets(adj, stepsRemaining - 1); // repeat until room or 1 left
			}
			visited.remove(adj); // remove backtrack 
		}
	}
}
