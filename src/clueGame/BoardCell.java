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
	private Set<BoardCell> adjList = new HashSet<BoardCell>();

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

	public void setAdjList(Set<BoardCell> adjList) {
		this.adjList = adjList;
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

	public DoorDirection getDoorDirection() {
		// TODO Auto-generated method stub
		return doorDirection;
	}

	public boolean isLabel() {
		// TODO Auto-generated method stub
		return roomLabel;
	}

	public boolean isRoomCenter() {
		// TODO Auto-generated method stub
		return roomCenter;
	}

	public char getSecretPassage() {
		// TODO Auto-generated method stub
		return secretPassage;
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

	public void setOccupied(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public boolean getOccupied() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getRoom() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasSecretPassage() {
		return (secretPassage != '0');
	}
}
