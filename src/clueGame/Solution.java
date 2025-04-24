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
//		if (this.person.equals(solution.getPerson())) {
//			System.out.println("Person is correct");
//		} else {
//			System.out.println(this.person.getCardName() + " does not equal " + solution.getPerson().getCardName());
//		}
//		if (this.weapon.equals(solution.getWeapon())) {
//			System.out.println("Weapon is correct");
//		} else {
//			System.out.println(this.weapon.getCardName() + " does not equal " + solution.getWeapon().getCardName());
//		}
//		if (this.room.equals(solution.getRoom())) {
//			System.out.println("Room is correct");
//		} else {
//			System.out.println(this.room.getCardName() + " does not equal " + solution.getRoom().getCardName());
//		}
		return this.person.equals(solution.getPerson()) && this.weapon.equals(solution.getWeapon()) && this.room.equals(solution.getRoom());
    }
}
