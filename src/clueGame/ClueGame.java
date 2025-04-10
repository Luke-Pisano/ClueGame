package clueGame;

import javax.swing.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public class ClueGame extends JFrame {
    private Board board = Board.getInstance();
    JFrame clueGame = new JFrame();
    GameCardsPanel cardsPanel = new GameCardsPanel();
    GameControlPanel controlPanel = new GameControlPanel();

    public ClueGame() {

    }

    /**
     * Gets the size that the cell should have. Each cell is a square, so only one dimension is returned.
     * @return the dimensions of the cell
     */
    public int getCellSize() {
        // TODO fix logic
        int minDim;
        int cellDim;
        if ((clueGame.getX() - cardsPanel.getX()) > (clueGame.getY() - controlPanel.getY())) {
            minDim = clueGame.getY();
        } else {
            minDim = clueGame.getX();
        }
        if (board.getNumColumns() < board.getNumRows()) {
            cellDim = minDim / board.getNumRows();
        } else {
            cellDim = minDim / board.getNumColumns();
        }
        return cellDim;
    }
}
