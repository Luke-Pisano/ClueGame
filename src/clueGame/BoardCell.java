package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;

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
		this.adjList = new HashSet<>();
	}

	public BoardCell(int i, int j) {
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * @param adj BoardCell to be added to this cell's list of adjacent cells.
	 */
	public void addAdj(BoardCell adj) {
		adjList.add(adj);
	}

	/**
	 *
	 * @return Set of all cells adjacent to this cell.
	 */
	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	/**
	 *
	 * @return Whether the cell is a doorway.
	 */
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

	public Object getDoorDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLabel() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRoomCenter() {
		// TODO Auto-generated method stub
		return false;
	}

	public char getSecretPassage() {
		// TODO Auto-generated method stub
		return 0;
	}
}
