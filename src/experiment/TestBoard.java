package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private final int NUMROWS = 4;     // Temporary value until we know what it actually is
	private final int NUMCOLS = 4;     // Temporary value until we know what it actually is
	private TestBoardCell[][] board;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;

	public TestBoard() {
		board = new TestBoardCell[NUMROWS][NUMCOLS]; // Initialize board array
		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				board[row][col] = new TestBoardCell(row, col);
			}
		}
		calcAdj(); // build adjacency lists
	}

	// method to build adjacency lists
	public void calcAdj() {
		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				// check if cell above, below, left or right
				TestBoardCell cell = board[row][col];
				if (row < NUMROWS - 1) {
					cell.addAdjacency(board[row + 1][col]);
				}
				if (row > 0) {
					cell.addAdjacency(board[row - 1][col]); 
				}
				if (col < NUMCOLS - 1) {
					cell.addAdjacency(board[row][col + 1]);
				}
				if (col > 0) {
					cell.addAdjacency(board[row][col - 1]);
				}
			}
		}
	}

	// method to call findAllTargets method
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		targets = new HashSet<>();
		visited = new HashSet<>();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	// recursive algorithm to find all possible cells can end on
	private void findAllTargets(TestBoardCell cell, int stepsRemaining) {
		for (TestBoardCell adj : cell.getAdjList()) {
			if (visited.contains(adj)) {
				continue;
			}
			if (adj.getOccupied()) {
				continue;
			}
			visited.add(adj);

			if (stepsRemaining == 1) {
				targets.add(adj);
			} else if (adj.getRoom()){
				targets.add(adj);
			} else {
				findAllTargets(adj, stepsRemaining - 1);
			}
			visited.remove(adj);
		}
	}

	/**
	 *
	 * @param row - row of requested cell
	 * @param col - column of requested cell
	 * @return the cell at the requested row/column
	 */
	public TestBoardCell getCell(int row, int col) {
		for (int rw = 0; rw < NUMROWS; rw++) {
			for (int cl = 0; cl < NUMCOLS; cl++) {
				if (board[rw][cl].row == row && board[rw][cl].col == col) {
					return board[rw][cl];
				}
			}
		}
		System.out.println("ERROR: Cell not found. Check row/col input.");
		return null;
	}

	public Set<TestBoardCell> getTargets() {
		return targets;
	}
}
