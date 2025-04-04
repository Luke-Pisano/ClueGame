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

	@Override
	public Solution createSuggestion() {
		// TODO: Implement the logic to create a suggestion
		return null;
	}

	@Override
	public Card disproveSuggestion(Solution suggestion) {
		// TODO: Implement the logic to disprove a suggestion
		// TODO: Disproves random for now
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
}
