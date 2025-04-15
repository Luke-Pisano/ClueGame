package tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ComputerAITest {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load config files
		board.initialize();
	}

	// test computer player is setup correctly
	@Test
	public void testComputerPlayer() {
		// Create a ComputerPlayer instance
		ComputerPlayer computerPlayer = new ComputerPlayer("AI", "Blue", 0, 0);

		// Check if the player is initialized correctly
		assertEquals("AI", computerPlayer.getName());
		assertEquals(Color.BLUE, computerPlayer.getColor());
		assertEquals(0, computerPlayer.getRow());
		assertEquals(0, computerPlayer.getColumn());

	}

	// test suggestion uses current room
	@Test
	public void testCorrectRoomSuggestion() {
		ComputerPlayer player = new ComputerPlayer("AI", "Blue", 0, 0);
		player.setRoom("Jail");
		
		Solution suggestion = player.createSuggestion();
		
		assertEquals("Jail", suggestion.getRoom().getCardName());
	}



	@Test
	public void testOnlyOneUnseenWeaponOrPersonSelected() {		
		ComputerPlayer player = new ComputerPlayer("AI", "Blue", 0, 0);
		player.setRoom("Jail");

		for (Card c : board.getDeck()) {
			if (c.getCardType() == CardType.WEAPON && !c.getCardName().equals("Revolver")) {
				player.updateSeenCards(c);
			} else if (c.getCardType() == CardType.PERSON && !c.getCardName().equals("Buffalo Bill")) {
				player.updateSeenCards(c);
			}
		}

		Solution suggestion = player.createSuggestion();
		assertEquals("Revolver", suggestion.getWeapon().getCardName());
		assertEquals("Buffalo Bill", suggestion.getPerson().getCardName());
		assertEquals("Jail", suggestion.getRoom().getCardName());

	}

	@Test
	public void testMultipleUnseenWeaponPersonRandomlySelected() {
		ComputerPlayer player = new ComputerPlayer("AI", "Blue", 0, 0);
		player.setRoom("Jail");

		Set<String> weaponChoices = new HashSet<>();
		Set<String> personChoices = new HashSet<>();

		for (int i = 0; i < 100; i++) {
			Solution suggestion = player.createSuggestion();
			weaponChoices.add(suggestion.getWeapon().getCardName());
			personChoices.add(suggestion.getPerson().getCardName());
		}

		assertTrue(weaponChoices.size() > 1);
		assertTrue(personChoices.size() > 1);
	}

	@Test
	public void testSelectTargetNoRooms() {
		ComputerPlayer player = new ComputerPlayer("AI", "Blue", 5, 5); 

	    BoardCell startCell = board.getCell(8, 13);

	    board.calcTargets(startCell, 1);
	    
	    Set<BoardCell> targets = board.getTargets();

	    Set<BoardCell> results = new HashSet<>();
	    for (int i = 0; i < 50; i++) {
	        results.add(player.selectTarget(targets));
	    }

	    assertTrue(results.size() > 1);
	}

	@Test
	public void testSelectTargetRoomUnseenSelected() {
	    ComputerPlayer player = new ComputerPlayer("AI", "Blue", 2, 4);

	    BoardCell startCell = board.getCell(2, 7);
	    board.calcTargets(startCell, 2);
	    Set<BoardCell> targets = board.getTargets();

	    BoardCell roomCell = board.getCell(2, 3);
	    assertTrue(targets.contains(roomCell));

	    BoardCell selected = player.selectTarget(targets);

	    assertEquals(roomCell, selected);
	}

	@Test
	public void testSelectTargetRoomSeenThenRandom() {
	    ComputerPlayer player = new ComputerPlayer("AI", "Blue", 2, 4);

	    BoardCell startCell = board.getCell(2, 4);
	    board.calcTargets(startCell, 2);
	    Set<BoardCell> targets = board.getTargets();

	    Card seenRoom = new Card("Jail", CardType.ROOM);
	    player.updateSeenCards(seenRoom);

	    Set<BoardCell> selectedCells = new HashSet<>();
	    for (int i = 0; i < 50; i++) {
	        selectedCells.add(player.selectTarget(targets));
	    }
	    assertTrue(selectedCells.size() > 1);
	}

}