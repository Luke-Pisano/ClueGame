package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public Card(String name, CardType type) {
		this.cardName = name;
		this.cardType = type;
	}
	
	/**
	 * Checks to see if two cards are equal in type and value
	 * @param target card
	 * @return boolean of if the target card is equal to current card
	 */
	public boolean equals(Card target) {
        return cardName.equals(target.cardName) && cardType == target.cardType;
	}
	
    // Getter for cardName
	public String getCardName() {
        return cardName;
    }

    // Getter for cardType
    public CardType getCardType() {
        return cardType;
    }
}
