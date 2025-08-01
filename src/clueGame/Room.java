package clueGame;

public class Room {
	private final String name;
	private BoardCell centerCell;
	private BoardCell labelCell;

	/**
	 *
	 * @param name Name of the room.
	 */
	public Room(String name) {
		this.name = name;
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
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
}
