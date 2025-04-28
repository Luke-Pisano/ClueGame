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

	public boolean myEquals(Solution solution) {
		return this.person.equals(solution.getPerson()) && this.weapon.equals(solution.getWeapon()) && this.room.equals(solution.getRoom());
    }
}
