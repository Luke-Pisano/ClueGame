package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Solution;
import clueGame.Player;

public class GameSolutionTest {
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load config files 
		board.initialize();
	}	
	
	// Check accusations is correct
	@Test
	public void testCheckAccusation() {
        // Create a solution with known values
        Card room = new Card("Library", CardType.ROOM);
        Card person = new Card("Miss Scarlet", CardType.PERSON);
        Card weapon = new Card("Revolver", CardType.WEAPON);
        Solution solution = new Solution(room, person, weapon);
        
        // Create cards that aren't solution
        Card roomWrong = new Card("Cemetery", CardType.ROOM);
		Card personWrong = new Card("Johnathan Karsh", CardType.PERSON);
		Card weaponWrong = new Card("Dynamite", CardType.WEAPON);

        // Check if the solution is created correctly
        assertEquals(room, solution.getRoom());
        assertEquals(person, solution.getPerson());
        assertEquals(weapon, solution.getWeapon());
        
        // check solution has wrong room
        assertNotEquals(roomWrong, solution.getRoom());
		
		// check solution has wrong person
        assertNotEquals(personWrong, solution.getPerson());
		
		// check solution has wrong weapon
        assertNotEquals(weaponWrong, solution.getWeapon());
    }
	
	// test if a player has one matching card to disprove suggestion
	@Test
	public void testDisproveSuggestionOne() {
		Player player = new ComputerPlayer("TestPlayer", "RED", 0, 0);
		Card matchingCard = new Card("Revolver", CardType.WEAPON);
		Card nonMatching1 = new Card("Jail", CardType.ROOM);
		Card nonMatching2 = new Card("Johnathan Karsh", CardType.PERSON);
		
		player.addCard(matchingCard);
		player.addCard(nonMatching1);
		player.addCard(nonMatching2);

		Solution suggestion = new Solution(
			new Card("Shovel", CardType.WEAPON),
			new Card("Church", CardType.ROOM),
			matchingCard
		);
		
		Card result = player.disproveSuggestion(suggestion);
		assertEquals(matchingCard, result);
	}
	
	// test if player has more than 1 matching card is truly random
	@Test
	public void testDisproveSuggestionMultiple() {
		board.clearData();

		Player player = new ComputerPlayer("TestPlayer", "RED", 0, 0);
		Card match1 = new Card("Revolver", CardType.WEAPON);
		Card match2 = new Card("Jail", CardType.ROOM);
		Card nonMatching = new Card("Johnathan Karsh", CardType.PERSON);
		
		player.addCard(match1);
		player.addCard(match2);
		player.addCard(nonMatching);

		Solution suggestion = new Solution(
			match1,
			new Card("Plum", CardType.PERSON),
			match2
		);

		Set<Card> results = new HashSet<>();
		for (int i = 0; i < 50; i++) {
			Card result = player.disproveSuggestion(suggestion);
			results.add(result);
		}

		assertTrue(results.contains(match1));
		assertTrue(results.contains(match2));
		assertEquals(2, results.size());
	}
	
	// test if a player has no matching cards to disprove suggestion
	@Test
	public void testDisproveSuggestionZero() {
		board.clearData();

		Player player = new ComputerPlayer("TestPlayer", "RED", 0, 0);
		Card nonMatching1 = new Card("Revolver", CardType.WEAPON);
		Card nonMatching2 = new Card("Jail", CardType.ROOM);
		Card nonMatching3 = new Card("Johnathan Karsh", CardType.PERSON);
		
		player.addCard(nonMatching1);
		player.addCard(nonMatching2);
		player.addCard(nonMatching3);

		Solution suggestion = new Solution(
			new Card("Shovel", CardType.WEAPON),
			new Card("Church", CardType.ROOM),
			new Card("Buffalo Bill", CardType.PERSON)
		);
		
		Card result = player.disproveSuggestion(suggestion);
		assertEquals(null, result);
	}

	// test suggestion no one can disprove
	// fails because method now reads suggestion from panel
	@Test
	public void testHandleSuggestionNoOneCanDisprove() {
		board.clearData();

		Player player1 = new ComputerPlayer("A", "RED", 0, 0);
		Player player2 = new ComputerPlayer("B", "BLUE", 0, 0);
		Player player3 = new ComputerPlayer("C", "GREEN", 0, 0);

		player1.addCard(new Card("Lasso", CardType.WEAPON));
		player2.addCard(new Card("Church", CardType.ROOM));
		player3.addCard(new Card("Buffalo Bill", CardType.PERSON));

		board.setPlayer(player1);
		board.setPlayer(player2);
		board.setPlayer(player3);

		Solution suggestion = new Solution(
			new Card("Revolver", CardType.WEAPON),
			new Card("Saloon", CardType.ROOM),
			new Card("Jesse James", CardType.PERSON)
		);

		Card result = board.handleSuggestion(suggestion, player1);
		assertEquals(null, result);
	}
	
	// test if no one has cards except suggester
	// fails because method now reads suggestion from panel
	@Test
	public void testHandleSuggestionOnlySuggestingPlayerCanDisprove() {
		board.clearData();

		Player player1 = new ComputerPlayer("A", "RED", 0, 0);
		Player player2 = new ComputerPlayer("B", "BLUE", 0, 0);
		Player player3 = new ComputerPlayer("C", "GREEN", 0, 0);

		player1.addCard(new Card("Revolver", CardType.WEAPON));
		board.setPlayer(player1);
		board.setPlayer(player2);
		board.setPlayer(player3);

		Solution suggestion = new Solution(
			new Card("Revolver", CardType.WEAPON),
			new Card("Church", CardType.ROOM),
			new Card("Buffalo Bill", CardType.PERSON)
		);

		Card result = board.handleSuggestion(suggestion, player1);
		assertEquals(new Card("Revolver", CardType.WEAPON), result);
	}

	// test if only the human can disprove suggestion
	// fails because method now reads suggestion from panel
	@Test
	public void testHandleSuggestionOnlyHumanCanDisprove() {
		board.clearData();

		Player player1 = new ComputerPlayer("A", "RED", 0, 0);
		Player player2 = new HumanPlayer("B", "BLUE", 0, 0);
		Player player3 = new ComputerPlayer("C", "GREEN", 0, 0);

		player2.addCard(new Card("Revolver", CardType.WEAPON));

		board.setPlayer(player1);
		board.setPlayer(player2);
		board.setPlayer(player3);


		Solution suggestion = new Solution(
			new Card("Revolver", CardType.WEAPON),
			new Card("Church", CardType.ROOM),
			new Card("Buffalo Bill", CardType.PERSON)
		);

		Card result = board.handleSuggestion(suggestion, player3);		
		assertTrue(result.equals(new Card("Revolver", CardType.WEAPON)));
	}

	// test that disproved by first player
	// fails because method now reads suggestion from panel
	@Test
	public void testHandleSuggestionTwoCanDisproveFirstReturns() {
		board.clearData();

		Player player1 = new ComputerPlayer("A", "RED", 0, 0);
		Player player2 = new ComputerPlayer("B", "BLUE", 0, 0);
		Player player3 = new ComputerPlayer("C", "GREEN", 0, 0);

		Card bill = new Card("Buffalo Bill", CardType.PERSON);
		Card revolver = new Card("Revolver", CardType.WEAPON);

		player2.addCard(bill);
		player3.addCard(revolver);

		board.setPlayer(player1);
		board.setPlayer(player2);
		board.setPlayer(player3);

		Solution suggestion = new Solution(
			bill,
			new Card("Hall", CardType.ROOM),
			revolver
		);

		Card result = board.handleSuggestion(suggestion, player1);

		assertEquals(bill, result);
	}
}