package tests;
 
 import clueGame.*;
 import org.junit.jupiter.api.BeforeAll;
 import org.junit.jupiter.api.Test;
 
 import java.util.Set;
 
 import static org.junit.Assert.*;
 
 public class ComputerAITest {
     private static Board board;
 
     @BeforeAll
     public static void setUp() {
         // Board is singleton, get the only instance
         board = Board.getInstance();
         // set the file names to use my config files
         board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
         // Initialize will load config files
         board.initialize();
     }
 
     @Test
     public void testComputerPlayer() {
         // Create a ComputerPlayer instance
         ComputerPlayer computerPlayer = new ComputerPlayer("AI", "Blue", 0, 0);
 
         // Check if the player is initialized correctly
         assertEquals("AI", computerPlayer.getName());
         assertEquals("Blue", computerPlayer.getColor());
         assertEquals(0, computerPlayer.getRow());
         assertEquals(0, computerPlayer.getColumn());
 
     }
 
 }

//	Computer player create a suggestion, tests include:
//	Room matches current location
//	If only one weapon not seen, it's selected
//	If only one person not seen, it's selected (can be same test as weapon)
//	If multiple weapons not seen, one of them is randomly selected
//	If multiple persons not seen, one of them is randomly selected
//Computer player select a target, tests include:
//	if no rooms in list, select randomly
//	if room in list that has not been seen, select it
//	if room in list that has been seen, each target (including room) selected randomly

