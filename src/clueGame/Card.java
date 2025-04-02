package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public Card(String name, CardType type) {
		this.cardName = name;
		this.cardType = type;
	}
	
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
