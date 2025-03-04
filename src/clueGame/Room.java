package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;

	public Room(String name) {
		this.name = name;
	}

	public Room(String name2, boolean equals) {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public BoardCell getLabelCell() {
		// TODO Auto-generated method stub
		return null;
	}

	public BoardCell getCenterCell() {
		// TODO Auto-generated method stub
		return null;
	}
}
