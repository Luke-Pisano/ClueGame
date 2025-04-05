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
		Board board = Board.getInstance();
		
		BoardCell currentCell = board.getCell(getRow(), getColumn());
		String roomName = board.getRoom(currentCell).getName();
		Card roomCard = new Card(roomName, CardType.ROOM);
		
		
		List<Card> allPeople = new ArrayList<>();
	    List<Card> allWeapons = new ArrayList<>();
	    
		List<Card> unseenPeople = new ArrayList<>();
	    List<Card> unseenWeapons = new ArrayList<>();
	    
	    for (Card card : board.getDeck()) {
	    	if (card.getCardType() == CardType.PERSON) {
	    		allPeople.add(card);
	    	} else if (card.getCardType() == CardType.WEAPON) {
	    		allWeapons.add(card);
	    	}
	    }
	    
	    for (Card card : allPeople) {
	        if (!seenCards.contains(card)) {
	            unseenPeople.add(card);
	        }
	    }

	    for (Card card : allWeapons) {
	        if (!seenCards.contains(card)) {
	            unseenWeapons.add(card);
	        }
	    }
	    
	    Collections.shuffle(unseenPeople);
	    Collections.shuffle(unseenWeapons);

	    
		/*
		 * 
		 * Add random ness to select other cards based on random card which hasn't been seen
		 * Have a set of type Cards of seen.
		 */
		
		return new Solution(roomCard, unseenPeople.get(0), unseenWeapons.get(0));

//		// Get current room based on player's location
//		BoardCell currentCell = board.getCell(getRow(), getColumn());
//		String roomName = board.getRoom(currentCell).getName();
//
//		Card roomCard = new Card(roomName, CardType.ROOM);
//		List<Card> unseenPeople = new ArrayList<>();
//		List<Card> unseenWeapons = new ArrayList<>();
//
//		// Loop through the deck
//		for (Card card : board.getDeck()) {
//			if (card.getCardType() == CardType.PERSON && !seenCards.contains(card)) {
//				unseenPeople.add(card);
//			} else if (card.getCardType() == CardType.WEAPON && !seenCards.contains(card)) {
//				unseenWeapons.add(card);
//			}
//		}
//
//		// Make sure we have at least one unseen person and weapon
//		if (roomCard == null || unseenPeople.isEmpty() || unseenWeapons.isEmpty()) {
//			return null; // Can't create suggestion
//		}
//
//		// Randomly pick one unseen person and weapon
//		Collections.shuffle(unseenPeople);
//		Collections.shuffle(unseenWeapons);
//
//		Card person = unseenPeople.get(0);
//		Card weapon = unseenWeapons.get(0);


	}
	
	public BoardCell selectTarget(Set<BoardCell> targets) {
	    List<BoardCell> targetList = new ArrayList<>(targets);
	    for (BoardCell target : targetList) {
	    	if(target.isRoomCenter()) {
	    		return target;
	    	}
	    }
	    Collections.shuffle(targetList); // randomizes the order
	    return targetList.get(0); // return the first one from the shuffled list
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
