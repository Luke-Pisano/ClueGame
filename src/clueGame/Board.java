package clueGame;

import static org.junit.Assert.assertEquals;

import java.sql.Array;
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
     public void initialize() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col] = new BoardCell(row, col);
			}
		}
		loadSetupConfig();
	}

	public void loadSetupConfig() {
		roomMap.put('S', new Room("Saloon"));
		roomMap.put('J', new Room("Jail"));
		roomMap.put('B', new Room("Bank"));
		roomMap.put('H', new Room("Hotel"));
		
		roomMap.put('C', new Room("Conservatory"));
		roomMap.put('B', new Room("Ballroom"));
		roomMap.put('R', new Room("Billiard Room"));
		roomMap.put('D', new Room("Dining Room"));
		roomMap.put('W', new Room("Walkway"));
     }

	/**
	 * Loads the Layout Config file which contains the board layout.
	 */
	public void loadLayoutConfig() {
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

			 Scanner reader = new Scanner(layout);
			 int row = 0;
			 while (reader.hasNextLine()) {
				 ArrayList<String> line = tokenize(reader.nextLine(), ",");
				 if (line.size() > 0) {
					 for (int col = 0; col < line.size(); col++) {
						 BoardCell temp = new BoardCell(row, col, 'D');
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
									 break;
								 case ('*'):
									 temp.setRoomCenter(true);
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
			ArrayList<String> result = new ArrayList<String>();

			// Case: if str is null
			 if (str == null || str.isEmpty()) {
				 return result; // Return empty list instead of list with empty string
			 }

			 // Case: if token is longer than the string
			 if (str.length() < token.length()) {
				 result.add(str);
				 return result;
			 }

			StringBuilder temp = new StringBuilder();
			for (int idx = 0; idx < str.length() - token.length(); idx++) {
				if (idx <= str.length() - token.length() && str.substring(idx, idx + token.length()).equals(token)) {
					result.add(temp.toString());
					temp = new StringBuilder();
					idx += token.length() - 1;
				} else {
					temp.append(str.charAt(idx));
				}
			}
			result.add(temp.toString());
			return result;
	 }

	/**
	 * Set the files to use for configuration of the game board.
	 * @param string The name of the config file used for layout.
	 * @param string2 The name of the config file used for setup.
	 */
	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
		layoutConfigFile = string;
		setupConfigFile = string2;
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
		return 0;
	}

	/**
	 *
	 * @return The number of columns contained on the board.
	 */
	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
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
