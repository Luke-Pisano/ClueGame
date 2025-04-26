package clueGame;

import javax.swing.*;

import java.awt.BorderLayout;

public class ClueGame extends JFrame {
    private static Board board = Board.getInstance();
    GameCardsPanel cardsPanel;
    GameControlPanel controlPanel;

    public ClueGame() {
    	setTitle("Clue Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);

        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();

        controlPanel = new GameControlPanel();
        cardsPanel = new GameCardsPanel();

        setLayout(new BorderLayout());
        add(board, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(cardsPanel, BorderLayout.EAST);

        board.setControlPanel(controlPanel);
        board.setGameCardsPanel(cardsPanel);

        setVisible(true);
        
        PlayMusic.playMusic();
    }

    public static void main(String[] args) {
        new ClueGame();

        new SplashScreen("You are " + board.getHumanPlayer().getName() + "\n\n"
                + "Can you find the solution\n"
                + "before the computer players?\n\n"
                + "Click OK to start the game.", "Welcome to Clue").showSplash();
        board.handleTurn();
    }
}
