package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

import experiment.TestBoard;
import experiment.TestBoardCell;

// This is the JUnit test class
public class BoardTestsExp {
    TestBoard board;

    @BeforeEach     // Run before each test, @BeforeALL also works
    public void setUp() {
        board = new TestBoard();
    }

    @Test
    public void testAdjacency() {
        TestBoardCell cell = board.getCell(0, 0);
        Set<TestBoardCell> testList = cell.getAdjList();
        Assert.assertTrue(testList.contains(board.getCell(1, 0)));
        Assert.assertTrue(testList.contains(board.getCell(0, 1)));
        Assert.assertEquals(2, testList.size());
    }

    @Test
    public void testTargetsNormal() {
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        Assert.assertEquals(6, targets.size());
        Assert.assertTrue(targets.contains(board.getCell(3, 0)));
        Assert.assertTrue(targets.contains(board.getCell(2, 1)));
        Assert.assertTrue(targets.contains(board.getCell(0, 1)));
        Assert.assertTrue(targets.contains(board.getCell(1, 2)));
        Assert.assertTrue(targets.contains(board.getCell(0, 3)));
        Assert.assertTrue(targets.contains(board.getCell(1, 0)));
    }

    @Test
    public void testTargetsMixed() {
        board.getCell(0, 2).setOccupied(true);
        board.getCell(1, 2).setRoom(true);
        TestBoardCell cell = board.getCell(0, 3);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        Assert.assertEquals(3, targets.size());
        Assert.assertTrue(targets.contains(board.getCell(1, 2)));
        Assert.assertTrue(targets.contains(board.getCell(2, 2)));
        Assert.assertTrue(targets.contains(board.getCell(3, 3)));
    }
}
