package experiment;

import java.util.Set;

public class TestBoard {
    private final int NUMROWS = 10;     // Temporary value until we know what it actually is
    private final int NUMCOLS = 10;     // Temporary value until we know what it actually is
    private TestBoardCell[][] board = new TestBoardCell[NUMROWS][NUMCOLS];
    private Set<TestBoardCell> targets;
    private int row, col;
    private Boolean isRoom, isOccupied ;

    public TestBoard() {
    	// create cells for the board for NUMROWS by NUMCOLS
        for (int rowIndex = 0; rowIndex < NUMROWS; rowIndex++) {
        	for (int colIndex = 0; colIndex < NUMCOLS; colIndex++) {
        		board[rowIndex][colIndex] = new TestBoardCell(rowIndex, colIndex);
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
