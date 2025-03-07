package clueGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
	/*
	 * variable and methods used for singleton pattern
	 */
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;

	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super();
		roomMap = new HashMap<>();
	}

	/**
	 *
	 * @return The instance of the game board. (There is only one instance of Board).
	 */
	public static Board getInstance() {
		return theInstance;
	}

	/**
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() throws BadConfigFormatException {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col] = new BoardCell(row, col);
			}
		}
		loadSetupConfig();
	}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loads the Layout Config file which contains the board layout.
	 */
	public void loadLayoutConfig() throws BadConfigFormatException {
		try {
			File layout = new File(layoutConfigFile);

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

			try (Scanner reader = new Scanner(layout)) {
				int row = 0;
				while (reader.hasNextLine()) {
					ArrayList<String> line = tokenize(reader.nextLine(), ",");
					if (line.size() > 0) {
						for (int col = 0; col < line.size(); col++) {
		                    if (line.get(col).isEmpty()) {
		                        throw new BadConfigFormatException("Empty column at: " + row + ", " + col);
		                    }
		                    
		                    char roomInitial = line.get(col).charAt(0);
		                    
		                    if (!roomMap.containsKey(roomInitial)) {
		                        //throw new BadConfigFormatException("Inital doesn't exist at: " + row + ", " + col);
		                    }
							BoardCell temp = new BoardCell(row, col, line.get(col).charAt(0));
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
									Room roomWithLabel = getRoom(temp.getInitial());
									roomWithLabel.setLabelCell(temp);
								break;
								case ('*'):
									temp.setRoomCenter(true);
									Room roomWithCenter = getRoom(temp.getInitial());
									roomWithCenter.setCenterCell(temp);
								break;
								default:
									// If length > 1 and no other cases occur, this cell must be a secret passage
									temp.setSecretPassage(line.get(col).charAt(1));
									break;
								}
							}
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
	 * @param string The name of the config file used for layout.
	 * @param string2 The name of the config file used for setup.
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
}
