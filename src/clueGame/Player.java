package clueGame;

import java.awt.Color;
import java.awt.Graphics;
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
	protected Color colorObj;
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
		convertColor();
	}

	public abstract Solution createSuggestion();

	public abstract Card disproveSuggestion(Solution suggestion);

	/**
	 * Draws the player on the board.
	 * @param graphics The graphics object to draw on.
	 * @param cellDimension The dimension of the cell. (width and height)
	 */
	public void draw(Graphics graphics, int cellDimension) {
        int positionCol = column * cellDimension;
        int positionRow = row * cellDimension;
        graphics.setColor(colorObj);
        graphics.fillOval(positionCol + 5, positionRow + 5, cellDimension - 10, cellDimension - 10);
        graphics.setColor(Color.BLACK);
        graphics.drawOval(positionCol + 5, positionRow + 5, cellDimension - 10, cellDimension - 10);
    }

	/**
	 * Converts the color string to a Color object.
	 */
	public void convertColor() {
		try {
			colorObj = (Color) Color.class.getField(color.toUpperCase()).get(null);
		} catch(Exception e) {
			System.out.println(System.out);
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
	
	public String getColor() {
		try {
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
