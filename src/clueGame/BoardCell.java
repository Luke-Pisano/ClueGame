package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private boolean isOccupied;
	private char secretPassage;
	private Set<BoardCell> adjList = new HashSet<>();
	private boolean highlightRoom = false;

	private boolean isTarget = false;
	
	/**
	 *
	 * @param row Row coordinate of cell.
	 * @param col Column coordinate of cell.
	 * @param initial Initial of the room type of the cell.
	 */
	public BoardCell(int row, int col, char initial) {
		this.row = row;
		this.col = col;
		this.initial = initial;
		this.doorDirection = DoorDirection.NONE;
		this.roomLabel = false;
		this.roomCenter = false;
		this.secretPassage = '0';
		this.isOccupied = false;
	}

	/**
	 * Draws the cell on the board.
	 * @param graphics The graphics object to draw on.
	 * @param row The row location of the cell.
	 * @param col The column location of the cell.
	 * @param cellDimension The dimension of the cell. (width and height)
	 */
	public void draw(Graphics graphics, int row, int col, int cellDimension) {
        int positionCol = col * cellDimension;
        int positionRow = row * cellDimension;

        if (isTarget && !roomCenter) {
        	graphics.setColor(Color.CYAN);
            graphics.fillRect(positionCol, positionRow, cellDimension, cellDimension);
            graphics.setColor(Color.BLACK);
            graphics.drawRect(positionCol, positionRow, cellDimension, cellDimension);
        } else if (highlightRoom) {
        	graphics.setColor(Color.CYAN);
            graphics.fillRect(positionCol, positionRow, cellDimension, cellDimension);
            //graphics.setColor(Color.BLACK);
            //graphics.drawRect(positionCol, positionRow, cellDimension, cellDimension);
        }
        else if (initial == 'W') {
            graphics.setColor(Color.YELLOW);
            graphics.fillRect(positionCol, positionRow, cellDimension, cellDimension);
            graphics.setColor(Color.BLACK);
            graphics.drawRect(positionCol, positionRow, cellDimension, cellDimension);
        } else if (initial == 'X'){
        	graphics.setColor(Color.BLACK);
            graphics.fillRect(positionCol, positionRow, cellDimension, cellDimension);
        } else {
            graphics.setColor(Color.GRAY);
            graphics.fillRect(positionCol, positionRow, cellDimension, cellDimension);
        }

        if (doorDirection != DoorDirection.NONE) {
            graphics.setColor(Color.BLUE);
            switch (doorDirection) {
                case UP -> graphics.fillRect(positionCol, positionRow, cellDimension, 5);
                case DOWN -> graphics.fillRect(positionCol, positionRow + cellDimension - 5, cellDimension, 5);
                case LEFT -> graphics.fillRect(positionCol, positionRow, 5, cellDimension);
                case RIGHT -> graphics.fillRect(positionCol + cellDimension - 5, positionRow, 5, cellDimension);
			default -> throw new IllegalArgumentException("Unexpected value: " + doorDirection);
            }
        }
    }

	/**
	 * @param adj BoardCell to be added to this cell's list of adjacent cells.
	 */
	public void addAdj(BoardCell adj) {
		adjList.add(adj);
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public void setAdjList(Set<BoardCell> adjList) {
		this.adjList = adjList;
	}

	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public char getInitial() {
		return initial;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public boolean isLabel() {
		return roomLabel;
	}
	public boolean isTarget() {
	    return isTarget;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public void setTarget(boolean value) {
	    isTarget = value;
	}
	
	public void setDoorDirection(DoorDirection dir) {
		doorDirection = dir;
	}

	public void setSecretPassage(char passage) {
		secretPassage = passage;
	}

	public void setRoomLabel(boolean val) {
		roomLabel = val;
	}

	public void setRoomCenter(boolean val) {
		roomCenter = val;
	}

	public void setOccupied(boolean val) {
		isOccupied = val;
	}
	
	public void setHighlightRoom(boolean highlightRoom) {
	    this.highlightRoom = highlightRoom;
	}

	public boolean getOccupied() {
		return isOccupied;
	}

	public boolean hasSecretPassage() {
		return (secretPassage != '0');
	}
}
