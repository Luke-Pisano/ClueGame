	package tests;

import org.junit.Assert;
import org.junit.Test;
import java.util.Set;
import org.junit.Before;

import experiment.TestBoard;
import experiment.TestBoardCell;

// This is the JUnit test class
public class BoardTestsExp {

	TestBoard board;

	// Run before each test, @BeforeALL also works
	@Before
	public void setUp() {
		board = new TestBoard();

	}

	// adjacency tests:

	// Test top left (0,0) adjacency (2 cells)
	@Test
	public void testAdjacencyTopLeft() {
		TestBoardCell cell = board.getCell(0, 0); // start 0,0
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 0))); // below
		Assert.assertTrue(testList.contains(board.getCell(0, 1))); // right
		Assert.assertEquals(2, testList.size());
	}

	// Test bottom right (3,3) adjacency (2 cells)
	@Test
	public void testAdjacencyBottomRight() {
		TestBoardCell cell = board.getCell(3, 3);// start 3,3
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 3))); //above
		Assert.assertTrue(testList.contains(board.getCell(3, 2))); // left
		Assert.assertEquals(2, testList.size());
	}

	// Test right edge (1,3) adjacency (3 cells)
	@Test
	public void testAdjacencyRightEdge() {
		TestBoardCell cell = board.getCell(1, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0, 3))); // above
		Assert.assertTrue(testList.contains(board.getCell(1, 2))); // left
		Assert.assertTrue(testList.contains(board.getCell(2, 3))); // below
		Assert.assertEquals(3, testList.size());
	}

	// Test left edge (3,0) adjacency (2 cells)
	@Test
	public void testAdjacencyLeftEdge() {
		TestBoardCell cell = board.getCell(3, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 0))); // above
		Assert.assertTrue(testList.contains(board.getCell(3, 1))); // right
		Assert.assertEquals(2, testList.size());
	}

	// Test middle cell (2,2) edge adjacency (4 cells)
	@Test
	public void testAdjacencyMiddle() {
		TestBoardCell cell = board.getCell(2, 2);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 2))); //above
		Assert.assertTrue(testList.contains(board.getCell(2, 1))); // left
		Assert.assertTrue(testList.contains(board.getCell(3, 2))); // below
		Assert.assertTrue(testList.contains(board.getCell(2, 3))); // right
		Assert.assertEquals(4, testList.size());
	}

	// calcTarget tests:

	// test targets normal
	@Test
	public void testTargetsNormal() {
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3); // move 3
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(6, targets.size()); // 6 total targets
		// check target positions
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
	}

	// test targets mixed
	@Test
	public void testTargetsMixed() {
		board.getCell(0, 2).setOccupied(true); // mark occupied
		board.getCell(1, 2).setRoom(true); // mark room
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3); // 3 cell movement
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
	}

	// test if the target is a room
	@Test
	public void testTargetsRoom() {
		board.getCell(1, 2).setRoom(true); // mark room
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
	}

	// test if cell occupied
	@Test
	public void testTargetsOccupied() {
		board.getCell(1, 1).setOccupied(true); // mark occupied
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2); // 2 steps
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
	}

	// test if they have a max roll
	@Test
	public void testTargetsRoll6() {
		TestBoardCell cell = board.getCell(0, 0); // Starting position
		board.calcTargets(cell, 6);  // roll 6
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(7, targets.size()); // 8 possible positions
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3))); 
	}


	// Test if they roll a 3
	@Test
	public void testTargetsRoll3() {
		TestBoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));

	}

}
