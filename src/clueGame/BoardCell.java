package clueGame;

import java.util.Set;

public class BoardCell {
	private int row;
	private int col;
	private char inital;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	
	public void addAdj(BoardCell adj) {
		
	}
}
