package clueGame;

public class ComputerPlayer extends Player {
	private String type = "COMPUTER";

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}
	
	public String getType() {
		return type;
	}
}
