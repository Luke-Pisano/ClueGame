package tests;

import clueGame.Board;
import clueGame.BoardCell;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class BoardAdjTargetTest {
	// TODO Adjacency tests
	//Locations with only walkways as adjacent locations
	//Locations within rooms not center. Note, this test is allowed to pass even for failing test.  (Should have empty adjacency list)
	//Locations that are at each edge of the board
	//Locations that are beside a room cell that is not a doorway
	//Locations that are doorways
	//Locations that are connected by secret passage
	//Target tests
	//Targets along walkways, at various distances
	//Targets that allow the user to enter a room
	//Targets calculated when leaving a room without secret passage
	//Targets calculated when leaving a room with secret passage
	//Targets that reflect blocking by other players

	// We make the Board static because we can load it one time and 
	// then do all the tests. 
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

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the jail which has two doors
		Set<BoardCell> testList = board.getAdjList(2, 2); // Random, non-center cell
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(6, 2)));
		assertTrue(testList.contains(board.getCell(2, 6)));
		
		// now test the livery stable which has 4 doors
		testList = board.getAdjList(2, 12); // This is the center of the room
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 13)));
		
		// one more room, the hotel which has a secret passage
		testList = board.getAdjList(7, 22);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(4, 25))); // This cell is a secret passage
		assertTrue(testList.contains(board.getCell(7, 18)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		Set<BoardCell> testList = board.getAdjList(6, 2);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(5, 2)));
		assertTrue(testList.contains(board.getCell(6, 3)));

		// Has second doorway as an adjacent cell, might need to edit later if that is not allowed
		testList = board.getAdjList(6, 14);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 13)));
		assertTrue(testList.contains(board.getCell(5, 14)));
		assertTrue(testList.contains(board.getCell(7, 14)));
		assertTrue(testList.contains(board.getCell(6, 12)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(28, 7);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(27, 7)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(19, 9);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(19, 8)));
		assertTrue(testList.contains(board.getCell(19, 10)));
		assertTrue(testList.contains(board.getCell(18, 9)));
		assertTrue(testList.contains(board.getCell(20, 9)));

		// Test adjacent to walkways
		testList = board.getAdjList(19, 17);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(19, 18)));
		assertTrue(testList.contains(board.getCell(19, 16)));
		assertTrue(testList.contains(board.getCell(18, 17)));
		assertTrue(testList.contains(board.getCell(20, 17)));

		// Test next to closet and room but not door
		testList = board.getAdjList(14,1);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(14, 2)));
		assertTrue(testList.contains(board.getCell(15, 1)));
	
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInJailRoom() {
		// test a roll of 1
		board.calcTargets(board.getCell(2, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(8, 2)));
		assertTrue(targets.contains(board.getCell(2, 6)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(2, 3), 3);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(7, 1)));
		assertTrue(targets.contains(board.getCell(3, 7)));
		assertTrue(targets.contains(board.getCell(2, 8)));
		assertTrue(targets.contains(board.getCell(8, 2)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(2, 3), 4);
		targets= board.getTargets();
		assertEquals(15, targets.size());
		assertTrue(targets.contains(board.getCell(0, 7)));
		assertTrue(targets.contains(board.getCell(1, 8)));
		assertTrue(targets.contains(board.getCell(7, 4)));
		assertTrue(targets.contains(board.getCell(6, 5)));
	}
	
	@Test
	public void testTargetsInKitchen() {
		// test a roll of 1
		board.calcTargets(board.getCell(7, 21), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(17, 18)));
		assertTrue(targets.contains(board.getCell(4, 25)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(7, 21), 2);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(4, 24)));
		assertTrue(targets.contains(board.getCell(3, 25)));
		assertTrue(targets.contains(board.getCell(6, 18)));
		assertTrue(targets.contains(board.getCell(7, 17)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(7, 21), 3);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(3, 24)));
		assertTrue(targets.contains(board.getCell(2, 25)));
		assertTrue(targets.contains(board.getCell(6, 17)));
		assertTrue(targets.contains(board.getCell(9, 18)));
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(2, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(2, 22)));
		assertTrue(targets.contains(board.getCell(3, 19)));
		assertTrue(targets.contains(board.getCell(1, 19)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(2, 19), 3);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(2, 22)));
		assertTrue(targets.contains(board.getCell(3, 19)));
		assertTrue(targets.contains(board.getCell(3, 17)));
		assertTrue(targets.contains(board.getCell(0, 18)));
		assertTrue(targets.contains(board.getCell(4, 18)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(2, 19), 4);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(2, 22)));
		assertTrue(targets.contains(board.getCell(2, 17)));
		assertTrue(targets.contains(board.getCell(4, 19)));
		assertTrue(targets.contains(board.getCell(4, 17)));
		assertTrue(targets.contains(board.getCell(5, 18)));
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(0, 7), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(1, 7)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(0, 7), 3);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 6)));
		assertTrue(targets.contains(board.getCell(2, 8)));
		assertTrue(targets.contains(board.getCell(3, 7)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(0, 7), 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(1, 6)));
		assertTrue(targets.contains(board.getCell(4, 7)));
		assertTrue(targets.contains(board.getCell(3, 8)));
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(8, 10), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(9, 10)));
		assertTrue(targets.contains(board.getCell(8, 11)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(8, 10), 3);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(6, 11)));
		assertTrue(targets.contains(board.getCell(10, 9)));
		assertTrue(targets.contains(board.getCell(11, 10)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(8, 10), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(8, 6)));
		assertTrue(targets.contains(board.getCell(7, 13)));
		assertTrue(targets.contains(board.getCell(12, 10)));
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(8, 10).setOccupied(true);
		board.calcTargets(board.getCell(13, 7), 4);
		board.getCell(8, 10).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(15, targets.size());
		assertTrue(targets.contains(board.getCell(8, 6)));
		assertTrue(targets.contains(board.getCell(7, 13)));
		assertTrue(targets.contains(board.getCell(12, 10)));
		assertFalse( targets.contains( board.getCell(10, 10))) ;
		assertFalse( targets.contains( board.getCell(8, 10))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(2, 6).setOccupied(true);
		board.getCell(2, 3).setOccupied(true);
		board.calcTargets(board.getCell(2, 6), 1);
		board.getCell(2, 6).setOccupied(false);
		board.getCell(2, 3).setOccupied(false);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(2, 6)));
		assertTrue(targets.contains(board.getCell(2, 7)));
		assertTrue(targets.contains(board.getCell(3, 6)));
		
		// check leaving a room with a blocked doorway
		board.getCell(2, 6).setOccupied(true);
		board.calcTargets(board.getCell(2, 3), 3);
		board.getCell(2, 6).setOccupied(false);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(7, 1)));
		assertTrue(targets.contains(board.getCell(7, 3)));
		assertTrue(targets.contains(board.getCell(6, 4)));

	}
}
