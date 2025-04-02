package tests;

/*
 * This program tests that config files are loaded properly.
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.DoorDirection;
import clueGame.Room;
import clueGame.Player;

public class FileInitTests {
	// Constants that I will use to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 29;
	public static final int NUM_COLUMNS = 26;
	public static final int NUM_DOORWAYS = 22;

	// NOTE: I made Board static because I only want to set it up one
	// time (using @BeforeAll), no need to do setup before each test.
	private static Board board;

	@BeforeAll
	public static void setUp() throws BadConfigFormatException {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Saloon", board.getRoom('S').getName() );
		assertEquals("Jail", board.getRoom('J').getName() );
		assertEquals("Bank", board.getRoom('B').getName() );
		assertEquals("Hotel", board.getRoom('H').getName() );
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
	// two cells that are not a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(2, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(6, 13);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(2, 19);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(8, 5);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(12, 9);
		assertFalse(cell.isDoorway());
		// Test room cell that is not a doorway
		cell = board.getCell(4, 12);
		assertFalse(cell.isDoorway());
	}


	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(NUM_DOORWAYS, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	// These cells are purple on planning spreadsheet
	@Test
	public void testRooms() {
		// just test a standard room location
		BoardCell cell = board.getCell( 2, 2);
		
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Jail" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;
		assertFalse( cell.getSecretPassage() == 'D' );

		// this is a label cell to test
		cell = board.getCell(1, 13);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Livery Stable" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );

		// this is a room center cell to test
		cell = board.getCell(7, 21);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Hotel" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

		// this is a secret passage test
		cell = board.getCell(22, 0);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Church" ) ;
		assertTrue( cell.getSecretPassage() == 'D' );

		// test a walkway
		cell = board.getCell(6, 1);
		room = board.getRoom( cell ) ;
		// Note for our purposes, walkways and closets are rooms
		assertTrue( room != null );
		assertEquals( room.getName(), "Walkway" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		assertFalse( cell.isDoorway()) ;
		assertFalse( cell.getSecretPassage() == 'D' );

		// test a closet space
		cell = board.getCell(10, 13);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		assertFalse( cell.isDoorway()) ;
		assertFalse( cell.getSecretPassage() == 'D' );
		
	}
	
	// These test that the players are loaded in correctly
	@Test
	public void testPlayers() {
		List<Player> players = board.getPlayers();

		assertEquals(6, players.size());
		
		assertEquals("Johnathan Karsh", players.get(0).getName());
		assertEquals("Calamity Jane", players.get(2).getName());
		assertEquals("Jesse James", players.get(5).getName());

		assertEquals("RED", players.get(1).getColor());
		assertEquals("YELLOW", players.get(3).getColor());
		assertEquals("ORANGE", players.get(5).getColor());
		
		assertEquals("HUMAN", players.get(0).getType());
		assertEquals("COMPUTER", players.get(2).getType());
		assertEquals("COMPUTER", players.get(5).getType());
		
		assertEquals(11, players.get(2).getRow());
		assertEquals(15, players.get(3).getRow());
		assertEquals(28, players.get(4).getRow());
		
		assertEquals(18, players.get(1).getColumn());
		assertEquals(0, players.get(3).getColumn());
		assertEquals(7, players.get(4).getColumn());
	}
		
	// These test that the cards are loaded in correctly
	@Test
	public void testCards() {
		List<Card> deck = board.getDeck();
		assertEquals(21, deck.size());
		
		Card cardToCompare = new Card("Saloon", CardType.ROOM);
				
		Card card1 = deck.get(0);
		Card card3 = deck.get(3);
		Card card13 = deck.get(13);

		assertTrue(card1.equals(cardToCompare));
		assertFalse(card3.equals(cardToCompare));
		assertFalse(card13.equals(cardToCompare));
	}
	
	/*
	 Tests to Add
	 
	 Test cards
	 	Test dealing out cards and selecting answer cards
	 	Test all players have same amount of cards
	 	Test cards not dealt twice
	 	
	 
	 
	 */
	

}
