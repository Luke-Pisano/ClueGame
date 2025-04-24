package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuggestionPanel extends JDialog {
    private static final long serialVersionUID = 1L;
    private Solution selectedSuggestion = null;

    /**
     * Creates a suggestion panel with the specified message and title.
     *
     */
    public SuggestionPanel(Room room) {
        setTitle("Make your suggestion");
        setModal(true);
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,1,10,10));

        JComboBox<String> roomSuggestion = new JComboBox<>();
        roomSuggestion.setEditable(false);
        roomSuggestion.addItem(room.getName());
        panel.add(new JLabel("Room:"));
        panel.add(roomSuggestion);

        JComboBox<String> playerSuggestion = new JComboBox<>();
        playerSuggestion.setEditable(false);
        for (Card card : Board.getInstance().getDeck()) {
            if (card.getCardType() == CardType.PERSON) {
                playerSuggestion.addItem(card.getCardName());
            }
        }
        panel.add(new JLabel("Person:"));
        panel.add(playerSuggestion);

        JComboBox<String> weaponSuggestion = new JComboBox<>();
        weaponSuggestion.setEditable(false);
        for (Card card : Board.getInstance().getDeck()) {
            if (card.getCardType() == CardType.WEAPON) {
                weaponSuggestion.addItem(card.getCardName());
            }
        }
        panel.add(new JLabel("Weapon:"));
        panel.add(weaponSuggestion);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2,10,10));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String roomName = (String) roomSuggestion.getSelectedItem();
                String personName = (String) playerSuggestion.getSelectedItem();
                String weaponName = (String) weaponSuggestion.getSelectedItem();

                Card room = new Card(roomName, CardType.ROOM);
                Card person = new Card(personName, CardType.PERSON);
                Card weapon = new Card(weaponName, CardType.WEAPON);

                selectedSuggestion = new Solution(room, person, weapon);
                dispose();
            }
        });
        buttonPanel.add(submitButton);
        add(panel, BorderLayout.CENTER);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public Solution getSuggestion() {
        return selectedSuggestion;
    }
    
    public static void main(String[] args) {
        SuggestionPanel suggestion = new SuggestionPanel(new Room("Kitchen"));
        suggestion.setVisible(true);
    }
}
