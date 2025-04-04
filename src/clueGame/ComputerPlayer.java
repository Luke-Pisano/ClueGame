package clueGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComputerPlayer extends Player {
	private String type = "COMPUTER";

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	@Override
	public Solution createSuggestion() {
		// Create a suggestion based on the computer player's hand and the game state
		// TODO Check implementation of suggestion algorithm
		Board board = Board.getInstance();
		Card room = null;
		Card person = null;
		Card weapon = null;
		for (Card card : board.getDeck()) {
			if (!this.seenCards.contains(card) && (room == null || person == null || weapon == null)) {
				if (card.getCardName().equals(board.getRoom(board.getCell(this.row, this.column)).getName())) {
					room = card;
				} else if (card.getCardType() == CardType.PERSON) {
					person = card;
				} else if (card.getCardType() == CardType.WEAPON) {
					weapon = card;
				}
			}
		}
		if (room == null || person == null || weapon == null) {
			// If we can't make a suggestion, return null
			return null;
		}
		return new Solution(room, person, weapon);
	}

	public BoardCell selectTarget(Set<BoardCell> targets) {
		// Select a target cell from the set of targets
		// TODO Implement a strategy for selecting a target
		return targets.iterator().next();
	}

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
}
