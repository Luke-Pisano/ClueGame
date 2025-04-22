package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuggestionPanel extends JDialog {
    private static final long serialVersionUID = 1L;

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
        SuggestionPanel suggestion = new SuggestionPanel(new Room("Kitchen"));
        suggestion.setVisible(true);
    }
}
