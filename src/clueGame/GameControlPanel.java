package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	
	private JTextField turn;
	private JTextField roll;
	private JTextField guess;
	private JTextField result;
	
	private JButton next;
	private JButton accusation;

		/**
		 * Constructor for the panel, it does 90% of the work
		 */
		public GameControlPanel()  {
			setLayout(new GridLayout(2, 0));

	        JPanel topPanel = new JPanel();
	        topPanel.setLayout(new GridLayout(1, 4));

	        // Current turn and roll
	        JPanel turnRollPanel = new JPanel();
	        turnRollPanel.setLayout(new GridLayout(2, 1));
	        JLabel turnLabel = new JLabel(" Whose turn?");
	        turn = new JTextField(10);
	        turn.setEditable(false);
	        turnRollPanel.add(turnLabel);
	        turnRollPanel.add(turn);

	        JPanel rollPanel = new JPanel();
	        JLabel rollLabel = new JLabel("Roll:");
	        roll = new JTextField(5);
	        roll.setEditable(false);
	        rollPanel.add(rollLabel);
	        rollPanel.add(roll);

	        // Buttons
	        next = new JButton("Next Player");
			ActionListener nextListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Board.getInstance().handleNextPlayer();
					} catch (TurnNotFinishedException e1) {
						new SplashScreen("Turn not finished", "Error").showSplash();
					}
				}
			};
			next.addActionListener(nextListener);
	        accusation = new JButton("Make Accusation");
			ActionListener accusationListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Handle next player action
					System.out.println("Accusation button clicked");
				}
			};
			accusation.addActionListener(accusationListener);

	        topPanel.add(turnRollPanel);
	        topPanel.add(rollPanel);
	        topPanel.add(next);
	        topPanel.add(accusation);

	        JPanel bottomPanel = new JPanel();
	        bottomPanel.setLayout(new GridLayout(0, 2));

	        JPanel guessPanel = new JPanel();
	        guessPanel.setBorder(new TitledBorder("Guess"));
	        guess = new JTextField(30);
	        guess.setEditable(false);
	        guessPanel.setLayout(new GridLayout(1, 0));
	        guessPanel.add(guess);

	        JPanel resultPanel = new JPanel();
	        resultPanel.setBorder(new TitledBorder("Guess Result"));
	        result = new JTextField(30);
	        result.setEditable(false);
	        resultPanel.setLayout(new GridLayout(1, 0));
	        resultPanel.add(result);

	        bottomPanel.add(guessPanel);
	        bottomPanel.add(resultPanel);

	        add(topPanel);
	        add(bottomPanel);
		}
		
		/**
		 * Main to test the panel
		 */
		public static void main(String[] args) {
			GameControlPanel panel = new GameControlPanel();  // create the panel
			JFrame frame = new JFrame();  // create the frame 
			frame.setContentPane(panel); // put the panel in the frame
			frame.setSize(750, 180);  // size the frame
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
			frame.setVisible(true); // make it visible
			
			// test filling in the data
			panel.setTurn(new ComputerPlayer( "Buffalo Bill", "RED", 0, 0), 5);
			panel.setGuess( "I have no guess!");
			panel.setGuessResult( "So you have nothing?");


		}

		// setters for information for panel
		private void setGuessResult(String result) {
			this.result.setText(result);
		}

		private void setGuess(String guess) {
			this.guess.setText(guess);
		}

		public void setTurn(Player computerPlayer, int roll) {
			this.turn.setText(computerPlayer.getName());
			this.roll.setText(String.valueOf(roll));
			this.turn.setBackground(computerPlayer.getColor());

		}
}
