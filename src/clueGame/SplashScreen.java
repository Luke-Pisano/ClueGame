package clueGame;

import javax.swing.*;

public class SplashScreen extends JOptionPane {
    private static final long serialVersionUID = 1L;
    private String title;

    public SplashScreen(String message, String title) {
        super(message, JOptionPane.INFORMATION_MESSAGE);
        this.title = title;
    }

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
