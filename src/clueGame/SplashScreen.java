package clueGame;

import javax.swing.*;

public class SplashScreen extends JOptionPane {
    private static final long serialVersionUID = 1L;
    private String title;

    /**
     * Creates a splash screen with the specified message and title.
     *
     * @param message The message to display on the splash screen.
     * @param title   The title of the splash screen.
     */
    public SplashScreen(String message, String title) {
        super(message, JOptionPane.INFORMATION_MESSAGE);
        this.title = title;
    }

    /**
     * Creates a dialog with the specified title and message.
     * Used only for testing purposes in main method.
     */
    public void showSplash() {
        JDialog dialog = createDialog(title);
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.dispose();
    }

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen("Welcome to Clue Game!\n\n"
                + "This is a simple game where you can solve a mystery.\n"
                + "Click OK to start the game.", "Clue Game");
        splash.showSplash();
    }
}
