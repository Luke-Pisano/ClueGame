package clueGame;

import java.util.Set;

public class HumanPlayer extends Player {
	private String type = "HUMAN";
	
	public HumanPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	@Override
	public Solution createSuggestion() {
		// TODO: Implement the logic to create a suggestion
		return null;
	}

	@Override
	public Card disproveSuggestion(Solution suggestion) {
		// TODO: Implement the logic to disprove a suggestion
		return null;
	}

	public String getType() {
		return type;
	}
}
