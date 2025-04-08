package clueGame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameControlPanel extends JPanel{
	
	private JTextField turn;
	private JTextField roll;
	private JTextField guess;
	private JTextField result;

		/**
		 * Constructor for the panel, it does 90% of the work
		 */
		public GameControlPanel()  {
			
		}
		
		
		/**
		 * Main to test the panel
		 * 
		 * @param args
		 */
		public static void main(String[] args) {
			GameControlPanel panel = new GameControlPanel();  // create the panel
			JFrame frame = new JFrame();  // create the frame 
			frame.setContentPane(panel); // put the panel in the frame
			frame.setSize(750, 180);  // size the frame
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
			frame.setVisible(true); // make it visible
			
			// test filling in the data
			panel.setTurn(new ComputerPlayer( "Col. Mustard", "ORANGE", 0, 0), 5);
			panel.setGuess( "I have no guess!");
			panel.setGuessResult( "So you have nothing?");
		}

		private void setGuessResult(String result) {
			this.result.setText(result);
		}

		private void setGuess(String guess) {
			this.guess.setText(guess);
		}

		private void setTurn(ComputerPlayer computerPlayer, int roll) {
			this.turn.setText(computerPlayer.getName());
		    this.roll.setText(String.valueOf(roll));
			
		}
}
