package clueGame;

public class HumanPlayer extends Player {
	private String type = "HUMAN";
	
	public HumanPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}
	
	public String getType() {
		return type;
	}
}
