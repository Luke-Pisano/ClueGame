package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Player {
	protected String name;
	protected Color color;
	protected int row;
	protected int column;
	protected List<Card> hand = new ArrayList<>();
	protected Set<Card> seenCards = new HashSet<>();
	protected String currentRoom;
	
	protected Player(String name, String inputColor, int row, int column) {
		this.name = name;
		this.row = row;
		this.column = column;
		convertColor(inputColor);
	}

	public abstract Solution createSuggestion();

	/**
	 * Disproves a suggestion made by another player.
	 * @param suggestion The suggestion to be disproved.
	 * @return A Card object representing the card that disproves the suggestion,
	 */
	public Card disproveSuggestion(Solution suggestion) {
		// Disprove the suggestion if possible
		List<Card> possibleCards = new ArrayList<>();
		
		for (Card card : getHand()) {
			if (card.equals(suggestion.getRoom()) || card.equals(suggestion.getPerson()) || card.equals(suggestion.getWeapon())) {
				updateSeenCards(card);
				possibleCards.add(card);
			}
		}
		
		if(!possibleCards.isEmpty()) {
			Collections.shuffle(possibleCards);
			return(possibleCards.get(0));
			
		}
		return null;
	}
	/**
	 * Draws the player on the board.
	 * @param graphics The graphics object to draw on.
	 * @param cellDimension The dimension of the cell. (width and height)
	 */
	public void draw(Graphics graphics, int cellDimension) {
        int positionCol = column * cellDimension;
        int positionRow = row * cellDimension;
        
        graphics.setColor(color);
        graphics.fillOval(positionCol + 5, positionRow + 5, cellDimension - 10, cellDimension - 10);
        graphics.setColor(Color.BLACK);
        graphics.drawOval(positionCol + 5, positionRow + 5, cellDimension - 10, cellDimension - 10);
    }

	/**
	 * Converts a string input to a Color object.
	 * @param inputColor The color name as a string.
	 */
	public void convertColor(String inputColor) {
		try {
			color = (Color) Color.class.getField(inputColor.toUpperCase()).get(null);
		} catch(Exception e) {
			System.err.println("Invalid color");
		}
	}
	
	public void setRoom(String roomName) {
		currentRoom = roomName;
	}
	
	public String getRoom() {
		return currentRoom;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public abstract String getType();

	public void addCard(Card card) {
		hand.add(card);
	}
	
	public List<Card> getHand() {
		return hand;
	}

	public void updateSeenCards(Card card) {
		seenCards.add(card);
	}

	public Set<Card> getSeenCards() {
		return seenCards;
	}
	
	public void setPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	protected abstract BoardCell selectTarget(Set<BoardCell> targets);
}
