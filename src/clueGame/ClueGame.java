package clueGame;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public class ClueGame extends JFrame {
    private Board board = Board.getInstance();
    JFrame clueGame = new JFrame();
    GameCardsPanel cardsPanel = new GameCardsPanel();
    GameControlPanel controlPanel = new GameControlPanel();

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

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ClueGame();
    }
}
