package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Player {
	protected String name;
	protected String color;
	protected int row, column;
	protected List<Card> hand = new ArrayList<>();
	protected Set<Card> seenCards = new HashSet<>();
	protected String currentRoom;
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}

	public abstract Solution createSuggestion();

	public abstract Card disproveSuggestion(Solution suggestion);

	public void updateHand(Card card) {
		// some method
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
	
	public String getColor() {
		try {
			Color colorObj = (Color) Color.class.getField(color.toUpperCase()).get(null);
			String hex = String.format("#%02x%02x%02x", colorObj.getRed(), colorObj.getGreen(), colorObj.getBlue());
			return hex;
		} catch(Exception e) {
			return("Invalid Color");
		}
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
}
