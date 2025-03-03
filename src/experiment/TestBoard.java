package experiment;

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
        calcAdj();
    }
    
    public void calcAdj() {
        for (int row = 0; row < NUMROWS; row++) {
        	for (int col = 0; col < NUMCOLS; col++) {
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
    

    public void calcTargets(TestBoardCell startCell, int pathlength) {
        /*
            TODO calculates legal targets for a move from startCell of length pathlength.
            create visited tracker that starts at zero
            for pathlength
                get adjacent cells
                append adjacent cells to visited list maybe

         */
        Set<TestBoardCell> allVisited = startCell.getAdjList();
        Set<TestBoardCell> lastVisited = startCell.getAdjList();
        allVisited.add(startCell);
        for (int numVisited = 1; numVisited < pathlength; numVisited++) {
            for (int cell = 0; cell < lastVisited.size(); cell++) {
            	
            }
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
                if (board[rw][cl]._row == row && board[rw][cl]._col == col) {
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
