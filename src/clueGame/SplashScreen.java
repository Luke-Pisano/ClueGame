package clueGame;

import javax.swing.*;

public class SplashScreen extends JOptionPane {
    private static final long serialVersionUID = 1L;
    private final String title;

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
     * This method displays a dialog box "splash screen"
     * with a specified title which is loaded in via the
     * constructor at object creation
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
