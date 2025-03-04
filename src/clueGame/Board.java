package clueGame;

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
     }
     
     
     public void initalize() {
    	 
     }
     
     public void loadSetupConfig() {
    	 
     }
     
     public void loadLayoutConfig() {
    	 
     }
     
     // this method returns the only Board
     public static Board getInstance() {
            return theInstance;
     }
     /*
      * initialize the board (since we are using singleton pattern)
      */
     public void initialize()
     {
     }
}
