package clueGame;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	private String name;
	private String color;
	private int row, column;
	private List<Card> hand = new ArrayList<>();
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}
	
	public void updateHand(Card card) {
		// some method
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
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
}
