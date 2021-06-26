package com.fintan.connectfive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


//handle two player session
public class SessionHandler implements Runnable {
	private Socket player1;
	private Socket player2;

	public char[][] cell = new char[6][9];

	public DataInputStream fromPlayer1;
	public DataOutputStream toPlayer1;
	public DataInputStream fromPlayer2;
	public DataOutputStream toPlayer2;

	public SessionHandler(Socket player1, Socket player2) {
		this.player1 = player1;
		this.player2 = player2;

		// Initialize cells
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 9; j++)
				cell[i][j] = ' ';
	}
	
	public void run() {
		try {
			// Create data input and output streams
			DataInputStream fromPlayer1 = new DataInputStream(player1.getInputStream());
			DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
			DataInputStream fromPlayer2 = new DataInputStream(player2.getInputStream());
			DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());

			// notify player 1 to start
			toPlayer1.writeInt(1);
			
			while (true) {
				// player 1 move
				int row = fromPlayer1.readInt();
				int column = fromPlayer1.readInt();
				char token;

				cell[row][column] = 'X';
				token = 'X';

				// Player 1 wins?
				if (isWon(row, column, token)) {
					toPlayer1.writeInt(GlobalConstants.PLAYER1_WON);
					toPlayer2.writeInt(GlobalConstants.PLAYER1_WON);
					sendMove(toPlayer2, row, column);
					break; 
				} else if (isFull(cell)) { 
					toPlayer1.writeInt(GlobalConstants.DRAW);
					toPlayer2.writeInt(GlobalConstants.DRAW);
					sendMove(toPlayer2, row, column);
					break;
				} else {
					// player 2 turn
					toPlayer2.writeInt(GlobalConstants.CONTINUE);
					// Send player 1 move to player 2
					sendMove(toPlayer2, row, column);
				}

				// Player 2 move
				row = fromPlayer2.readInt();
				column = fromPlayer2.readInt();
				cell[row][column] = 'O';
				token = 'O';

				// Player 2 wins?
				if (isWon(row, column, token)) {
					toPlayer1.writeInt(GlobalConstants.PLAYER2_WON);
					toPlayer2.writeInt(GlobalConstants.PLAYER2_WON);
					sendMove(toPlayer1, row, column);
					break;
				} else {
					// player 1 turn
					toPlayer1.writeInt(GlobalConstants.CONTINUE);

					// send player 2 move to player 1
					sendMove(toPlayer1, row, column);
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}


	


	/** Send the move to other player */
	private void sendMove(DataOutputStream toPlayer, int row, int column) throws IOException {
		toPlayer.write(row);
		toPlayer.write(column);
	}

	/** Determine if the cells are all occupied 
	 * @param cell */

	
	public static boolean isFull(char[][] cell) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 9; j++)
				if (cell[i][j] == ' ')
					return false;
		return true;
	}



	private boolean isWon(int row, int column, char token) {

		// Check Horizontal

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 4; y++) {
				if (cell[x][y] == token && cell[x][y + 1] == token && cell[x][y + 2] == token && cell[x][y + 3] == token
						&& cell[x][y + 4] == token) {
					return true;
				}
			}
		}
		/// Check Vertical

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 9; y++) {
				if (cell[x][y] == token && cell[x + 1][y] == token && cell[x + 2][y] == token && cell[x + 3][y] == token
						&& cell[x + 4][y] == token) {
					return true;
				}
			}
		}

		// Check Diagonal?

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 6; y++) {
				if (cell[x][y] == token && cell[x + 1][y + 1] == token && cell[x + 2][y + 2] == token
						&& cell[x + 3][y + 3] == token && cell[x + 4][y + 4] == token) {
					return true;
				}
			}
		}

		// Check Other Diagonal?
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y < 8; y++) {
				if (cell[x][y] == token && cell[x + 1][y - 1] == token && cell[x + 2][y - 2] == token
						&& cell[x + 3][y - 3] == token && cell[x + 4][y - 4] == token) {
					return true;
				}
			}
		}

		return false;
	}

}
