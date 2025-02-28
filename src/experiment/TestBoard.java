package experiment;

import java.util.Set;

public class TestBoard {
    private final int NUMROWS = 10;     // Temporary value until we know what it actually is
    private final int NUMCOLS = 10;     // Temporary value until we know what it actually is
    private TestBoardCell[][] board = new TestBoardCell[NUMROWS][NUMCOLS];
    private Set<TestBoardCell> targets;

    public TestBoard() {
        // Empty Constructor
    }

    public void calcTargets(TestBoardCell startCell, int pathlength) {
        /*
            To be implemented.
            calculates legal targets for a move from startCell of length pathlength.
         */
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
