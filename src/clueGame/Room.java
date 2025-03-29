package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;

	/**
	 *
	 * @param name Name of the room.
	 */
	public Room(String name) {
		this.name = name;
	}

	public Room(String name2, boolean equals) {
		// TODO Do we need this?
	}

	public String getName() {
		return name;
	}

	/**
	 * Sets the center of the room to a specific cell.
	 * @param centerCell The center cell of the room.
	 */
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	/**
	 * Sets the label location to a specific cell.
	 * @param labelCell The cell where the label of the room should be located.
	 */
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
}
