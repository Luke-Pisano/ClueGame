package clueGame;

import java.util.Set;

public class HumanPlayer extends Player {
	private String type = "HUMAN";
	
	public HumanPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	/**
	 * Creates a suggestion for the human player.
	 * @return A Solution object containing the suggestion.
	 */
	@Override
	public Solution createSuggestion() {
		// TODO: Implement the logic to create a suggestion
		return null;
	}

	public String getType() {
		return type;
	}

	@Override
	protected BoardCell selectTarget(Set<BoardCell> targets) {
		// TODO Auto-generated method stub
		return null;
	}
}
