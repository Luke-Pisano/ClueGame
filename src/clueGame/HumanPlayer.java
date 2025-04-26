package clueGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		return null;
	}

	/**
	 * Disproves a suggestion made by another player.
	 * @param suggestion The suggestion to be disproved.
	 * @return The card that disproves the suggestion, or null if the player cannot disprove it.
	 */
	@Override
	public Card disproveSuggestion(Solution suggestion) {
		// Disprove the suggestion if possible
		List<Card> possibleCards = new ArrayList<>();
		
		for (Card card : getHand()) {
			if (card.equals(suggestion.getRoom()) || card.equals(suggestion.getPerson()) || card.equals(suggestion.getWeapon())) {
				updateSeenCards(card);
				possibleCards.add(card);
			}
		}
		
		if(possibleCards.size() > 0) {
			Collections.shuffle(possibleCards);
			return(possibleCards.get(0));
			
		}
		return null;
	}

	public String getType() {
		return type;
	}

	@Override
	protected BoardCell selectTarget(Set<BoardCell> targets) {
		return null;
	}
}
