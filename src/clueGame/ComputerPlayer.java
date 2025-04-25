package clueGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private String type = "COMPUTER";
	private Random random = new Random(System.nanoTime());

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	/**
	 * Creates a suggestion for the computer player.
	 * The suggestion consists of a room, a person, and a weapon.
	 * The room is the current room of the player.
	 * The person and weapon are randomly selected from the unseen cards.
	 * @return A Solution object containing the suggestion.
	 */
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

		
		return new Solution(roomCard, unseenPeople.get(0), unseenWeapons.get(0));

	}

	/**
	 * Selects a target cell from the set of targets.
	 * The target is selected randomly, but if there are any room center cells,
	 * one of them is selected first.
	 * @param targets The set of target cells.
	 * @return A BoardCell object representing the selected target.
	 */
	public BoardCell selectTarget(Set<BoardCell> targets) {
	    List<BoardCell> targetList = new ArrayList<>(targets);
	    for (BoardCell target : targetList) {
	    	if(target.isRoomCenter()) {
	    		int randomNumber = random.nextInt(6) + 1;
	    		if (randomNumber % 2 == 0 ) {
		    		return target;	
	    		}
	    	}
	    }
	    Collections.shuffle(targetList); // randomizes the order
	    return targetList.get(0); // return the first one from the shuffled list
	}

	public String getType() {
		return type;
	}
}
