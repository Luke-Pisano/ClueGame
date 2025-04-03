package clueGame;

public class Solution {
	private Card room;
	private Card person;
	private Card weapon;
	
	
	public Solution(Card room, Card person, Card weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public Card getRoom() {
		return room;
	}

	public Card getPerson() {
		return person;
	}

	public Card getWeapon() {
		return weapon;
	}
}
