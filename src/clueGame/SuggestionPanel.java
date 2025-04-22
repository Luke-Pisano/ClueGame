package clueGame;

import javax.swing.*;

public class SuggestionPanel extends JOptionPane {
    private static final long serialVersionUID = 1L;
    private static final String title = "Make Your Suggestion";
    private JPanel panel = new JPanel();

    /**
     * Creates a suggestion panel with the specified message and title.
     *
     * @param message The message to display on the suggestion panel.
     */
    public SuggestionPanel(String message, Room room) {
        super(message, JOptionPane.INFORMATION_MESSAGE);

        JComboBox<String> roomSuggestion = new JComboBox<>();
        roomSuggestion.setEditable(false);
        roomSuggestion.addItem(room.getName());
        panel.add(roomSuggestion);

        JComboBox<String> playerSuggestion = new JComboBox<>();
        for (Card card : Board.getInstance().getDeck()) {
            if (card.getCardType() == CardType.PERSON) {
                playerSuggestion.addItem(card.getCardName());
            }
        }
        panel.add(playerSuggestion);

        JComboBox<String> weaponSuggestion = new JComboBox<>();
        for (Card card : Board.getInstance().getDeck()) {
            if (card.getCardType() == CardType.WEAPON) {
                weaponSuggestion.addItem(card.getCardName());
            }
        }
        panel.add(weaponSuggestion);
    }

    public static void main(String[] args) {
        SuggestionPanel suggestion = new SuggestionPanel("Make your suggestion", new Room("Kitchen"));
    }
}
