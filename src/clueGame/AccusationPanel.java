package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccusationPanel extends JDialog {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a suggestion panel with the specified message and title.
     *
     */
    public AccusationPanel() {
        setTitle("Make your accusation");
        setModal(true);
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,1,10,10));

        JComboBox<String> roomSuggestion = new JComboBox<>();
        roomSuggestion.setEditable(false);
        for (Card card : Board.getInstance().getDeck()) {
            if (card.getCardType() == CardType.ROOM) {
                roomSuggestion.addItem(card.getCardName());
            }
        }
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
                Card room = Board.toCard((String) roomSuggestion.getSelectedItem());
                Card person = Board.toCard((String) playerSuggestion.getSelectedItem());
                Card weapon = Board.toCard((String) weaponSuggestion.getSelectedItem());

                // Create a solution object with the selected values
                Solution accusation = new Solution(room, person, weapon);

                // Call the method to handle the accusation
                Board.getInstance().handleAccusation(accusation);
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

    public static void main(String[] args) {
        AccusationPanel accusation = new AccusationPanel();
        accusation.setVisible(true);
    }
}
