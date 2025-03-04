package clueGame;

import java.util.HashMap;
import java.util.Map;

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
            super() ;
            roomMap = new HashMap<>();
     }
     // this method returns the only Board
     public static Board getInstance() {
            return theInstance;
     }
     /*
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
     }
     
     public void loadLayoutConfig() {
    	 
     }

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
	    this.numRows = 28;
	    this.numColumns = 25;
	}


	public Room getRoom(char c) {
		
		// TODO Auto-generated method stub
		return roomMap.get(c);
	}


	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}


	public BoardCell getCell(int i, int j) {
		// TODO Auto-generated method stub
		return grid[i][j];
	}


	public Room getRoom(BoardCell cell) {
		// TODO Auto-generated method stub
		return getRoom(cell.getInitial());
	}
}
