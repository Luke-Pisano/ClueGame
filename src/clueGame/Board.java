package clueGame;

import static org.junit.Assert.assertEquals;

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
		grid = new BoardCell[numRows][numColumns]; // Allocate memory

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
     
     public void loadLayoutConfig() {
    	 
     }

	/**
	 * Set the files to use for configuration of the game board.
	 * @param string The name of the config file used for layout.
	 * @param string2 The name of the config file used for setup.
	 */
	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
	    this.numRows = 28;
	    this.numColumns = 25;
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
