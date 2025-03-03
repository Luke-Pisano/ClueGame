package experiment;

import java.util.Set;

public class TestBoardCell {
    public final int row;
    public final int col;
    private Set<TestBoardCell> adjList;
    private boolean isRoom;         // Whether the cell is a room
    private boolean isOccupied;     // Whether the cell is occupied
    
    /**
     * Constructor for creating a cell
     *
     * @param r - row of the cell's location
     * @param c - column of the cell's location
     */
    public TestBoardCell(int r, int c) {
        row = r;
        col = c;
        // By default, assume cell is not a room and is not occupied
        isRoom = false;
        isOccupied = false;
    }

    /**
     * Adds given cell to this cell's list of adjacent cells
     *
     * @param cell - cell to add that is adjacent to this cell
     */
    public void addAdjacency(TestBoardCell cell) {
        adjList.add(cell);
    }

    /**
     * Returns a list of all adjacent cells to this cell
     */
    public Set<TestBoardCell> getAdjList() {
        return adjList;
    }

    // Setters and Getters
    public void setRoom(boolean val) {
        isRoom = val;
    }

    public boolean getRoom() {
        return isRoom;
    }

    public void setOccupied(boolean val) {
        isOccupied = val;
    }

    public boolean getOccupied() {
        return isOccupied;
    }
}
