package clueGame;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GameCardsPanel extends JPanel {
	private JPanel knownCards;
	private JPanel people;
	private JPanel peopleInHand;
	private JPanel peopleSeen;
	private JPanel rooms;
	private JPanel roomsInHand;
	private JPanel roomsSeen;
	private JPanel weapons;
	private JPanel weaponsInHand;
	private JPanel weaponsSeen;

	public GameCardsPanel() {
		knownCards = new JPanel();
		knownCards.setBorder(new TitledBorder("Known Cards"));
		knownCards.setLayout(new GridLayout(3, 0));

		people = new JPanel();
		people.setBorder(new TitledBorder("People"));
		people.setLayout(new BoxLayout(people, BoxLayout.Y_AXIS));
		knownCards.add(people);
		peopleInHand = new JPanel();
		peopleInHand.setBorder(new TitledBorder("People in Hand"));
		peopleInHand.setLayout(new BoxLayout(peopleInHand, BoxLayout.Y_AXIS));
		peopleInHand.setPreferredSize(new Dimension(150, 50));
		people.add(peopleInHand);
		peopleSeen = new JPanel();
		peopleSeen.setBorder(new TitledBorder("People Seen"));
		peopleSeen.setLayout(new BoxLayout(peopleSeen, BoxLayout.Y_AXIS));
		people.add(peopleSeen);

		rooms = new JPanel();
		rooms.setBorder(new TitledBorder("Rooms"));
		rooms.setLayout(new BoxLayout(rooms, BoxLayout.Y_AXIS));
		knownCards.add(rooms);
		roomsInHand = new JPanel();
		roomsInHand.setBorder(new TitledBorder("Rooms in Hand"));
		roomsInHand.setLayout(new BoxLayout(roomsInHand, BoxLayout.Y_AXIS));
		rooms.add(roomsInHand);
		roomsSeen = new JPanel();
		roomsSeen.setBorder(new TitledBorder("Rooms Seen"));
		roomsSeen.setLayout(new BoxLayout(roomsSeen, BoxLayout.Y_AXIS));
		rooms.add(roomsSeen);

		weapons = new JPanel();
		weapons.setBorder(new TitledBorder("Weapons"));
		weapons.setLayout(new BoxLayout(weapons, BoxLayout.Y_AXIS));
		knownCards.add(weapons);
		weaponsInHand = new JPanel();
		weaponsInHand.setBorder(new TitledBorder("Weapons in Hand"));
		weaponsInHand.setLayout(new BoxLayout(weaponsInHand, BoxLayout.Y_AXIS));
		weapons.add(weaponsInHand);
		weaponsSeen = new JPanel();
		weaponsSeen.setBorder(new TitledBorder("Weapons Seen"));
		weaponsSeen.setLayout(new BoxLayout(weaponsSeen, BoxLayout.Y_AXIS));
		weapons.add(weaponsSeen);

		add(knownCards);

		initializeLists();
	}

	public void addInHandCard(Card card) {
		JTextField textField = new JTextField(card.getCardName());
		textField.setEditable(false);
		switch (card.getCardType()) {
			case PERSON:
				removeNone(peopleInHand);
				peopleInHand.add(textField);
				break;
			case ROOM:
				removeNone(roomsInHand);
				roomsInHand.add(textField);
				break;
			case WEAPON:
				removeNone(weaponsInHand);
				weaponsInHand.add(textField);
				break;
		}
		revalidate();
		repaint();
	}

	public void addSeenCard(Card card, Player player) {
		JTextField textField = new JTextField(card.getCardName());
		textField.setEditable(false);
		switch (card.getCardType()) {
			case PERSON:
				removeNone(peopleSeen);
				peopleSeen.add(textField);
				break;
			case ROOM:
				removeNone(roomsSeen);
				roomsSeen.add(textField);
				break;
			case WEAPON:
				removeNone(weaponsSeen);
				weaponsSeen.add(textField);
				break;
		}
		textField.setBackground(Color.decode(player.getColor()));
		revalidate();
		repaint();
	}

	public void initializeLists() {
		peopleInHand.removeAll();
		peopleSeen.removeAll();
		roomsInHand.removeAll();
		roomsSeen.removeAll();
		weaponsInHand.removeAll();
		weaponsSeen.removeAll();

		JTextField field = new JTextField("NONE");
		field.setEditable(false);
		peopleInHand.add(field);
		field = new JTextField("NONE");
		field.setEditable(false);
		peopleSeen.add(field);
		field = new JTextField("NONE");
		field.setEditable(false);
		roomsInHand.add(field);
		field = new JTextField("NONE");
		field.setEditable(false);
		roomsSeen.add(field);
		field = new JTextField("NONE");
		field.setEditable(false);
		weaponsInHand.add(field);
		field = new JTextField("NONE");
		field.setEditable(false);
		weaponsSeen.add(field);

		revalidate();
		repaint();
	}

	public void removeNone(JPanel panel) {
		JTextField field = (JTextField) panel.getComponent(0);
		if (field.getText().equals("NONE")) {
			panel.remove(field);
		}

		revalidate();
		repaint();
	}

	public static void main(String[] args) {
		// Test an empty panel
		GameCardsPanel emptyPanel = new GameCardsPanel();  // create the panel
		JFrame frame1 = new JFrame();  // create the frame
		frame1.setContentPane(emptyPanel); // put the panel in the frame
		frame1.setSize(300, 750);  // size the frame
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame1.setVisible(true); // make it visible

		// Test a panel with filled in cards
		GameCardsPanel panel = new GameCardsPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(300, 750);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		// Test filling in the panels
		// Test Player hand cards
		Card card1 = new Card("Miss Scarlet", CardType.PERSON);
		Card card2 = new Card("Revolver", CardType.WEAPON);
		Card card3 = new Card("Library", CardType.ROOM);
		panel.addInHandCard(card1);
		panel.addInHandCard(card2);
		panel.addInHandCard(card3);

		// Test seen cards
		Card card4 = new Card("Professor Plum", CardType.PERSON);
		Card card5 = new Card("Candlestick", CardType.WEAPON);
		Card card6 = new Card("Kitchen", CardType.ROOM);
		Card card7 = new Card("Buffalo Bill", CardType.PERSON);
		Card card8 = new Card("Dagger", CardType.WEAPON);
		panel.addSeenCard(card4, new ComputerPlayer("Player 1", "Red", 0, 0));
		panel.addSeenCard(card5, new ComputerPlayer("Player 2", "Blue", 0, 0));
		panel.addSeenCard(card6, new ComputerPlayer("Player 3", "Green", 0, 0));
		panel.addSeenCard(card7, new ComputerPlayer("Player 4", "Yellow", 0, 0));
		panel.addSeenCard(card8, new ComputerPlayer("Player 5", "Orange", 0, 0));
	}

}