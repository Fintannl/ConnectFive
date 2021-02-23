package com.fintan.connectfive;

import java.io.*;
import java.net.*;

public class ConnectFiveServer {

	static int port = GlobalConstants.INPUT_PORT;
	static ServerSocket serverSocket;


	private static void listen(int port) throws IOException {

		serverSocket = new ServerSocket(GlobalConstants.INPUT_PORT);
		serverSocket.setSoTimeout(100 * 1000); // timeout set to 100 seconds

		while (true) {
			try {
				// Connect to player 1
				System.out.println("Waiting for Player 1");
				Socket player1 = serverSocket.accept();
				System.out.println("Player 1 Connected");
				new DataOutputStream(player1.getOutputStream()).writeInt(GlobalConstants.PLAYER1);

				// Connect to player 2
				System.out.println("Waiting for Player 2");
				Socket player2 = serverSocket.accept();
				System.out.println("Player 2 Connected");
				new DataOutputStream(player2.getOutputStream()).writeInt(GlobalConstants.PLAYER2);

				HandleASession task = new HandleASession(player1, player2);
				new Thread(task).start();

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!. Game is ended");
				if (serverSocket != null && !serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace(System.err);
					}

				}
				break;

			} catch (IOException ex) {
				System.err.println(ex);
				if (serverSocket != null && !serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace(System.err);
					}

				}
				break;
			}

		}
	}

	public static void main(String[] args) throws IOException {

		System.out.println("I'm the Server, Game has Started, Waiting For Clients\n");
		listen(port);
	}

}

// Define the thread class for handling a new session for two players
class HandleASession implements Runnable {
	private Socket player1;
	private Socket player2;

	// Create and initialize cells
	private char[][] cell = new char[6][9];

	public DataInputStream fromPlayer1;
	public DataOutputStream toPlayer1;
	public DataInputStream fromPlayer2;
	public DataOutputStream toPlayer2;

	public HandleASession(Socket player1, Socket player2) {
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

			// Write anything to notify player 1 to start
			toPlayer1.writeInt(1);
			

			// Serve the players
			while (true) {
				// Receive a move from player 1
				int row = fromPlayer1.readInt();
				int column = fromPlayer1.readInt();
				char token;

				cell[row][column] = 'X';
				token = 'X';

				// Check if Player 1 wins
				if (isWon(row, column, token)) {
					toPlayer1.writeInt(GlobalConstants.PLAYER1_WON);
					toPlayer2.writeInt(GlobalConstants.PLAYER1_WON);
					sendMove(toPlayer2, row, column);
					break; // Break the loop
				} else if (isFull()) { // Check if all cells are filled
					toPlayer1.writeInt(GlobalConstants.DRAW);
					toPlayer2.writeInt(GlobalConstants.DRAW);
					sendMove(toPlayer2, row, column);
					break;
				} else {

					// Notify player 2 to take the turn
					toPlayer2.writeInt(GlobalConstants.CONTINUE);
					// Send player 1's selected row and column to player 2
					sendMove(toPlayer2, row, column);
				}

				// Receive a move from Player 2
				row = fromPlayer2.readInt();
				column = fromPlayer2.readInt();
				cell[row][column] = 'O';
				token = 'O';

				// Check if Player 2 wins
				if (isWon(row, column, token)) {
					toPlayer1.writeInt(GlobalConstants.PLAYER2_WON);
					toPlayer2.writeInt(GlobalConstants.PLAYER2_WON);
					sendMove(toPlayer1, row, column);
					break;
				} else {
					// Notify player 1 to take the turn
					toPlayer1.writeInt(GlobalConstants.CONTINUE);

					// Send player 2's selected row and column to player 1
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

	/** Determine if the cells are all occupied */
	private boolean isFull() {
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 9; j++)
				if (cell[i][j] == ' ')
					return false;
		return true;
	}



	private boolean isWon(int row, int column, char token) {

		// Horizontal wins?

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 4; y++) {
				if (cell[x][y] == token && cell[x][y + 1] == token && cell[x][y + 2] == token && cell[x][y + 3] == token
						&& cell[x][y + 4] == token) {
					return true;
				}
			}
		}
		/// Vertical Wins?

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 9; y++) {
				if (cell[x][y] == token && cell[x + 1][y] == token && cell[x + 2][y] == token && cell[x + 3][y] == token
						&& cell[x + 4][y] == token) {
					return true;
				}
			}
		}

		// Diagonal wins?

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 6; y++) {
				if (cell[x][y] == token && cell[x + 1][y + 1] == token && cell[x + 2][y + 2] == token
						&& cell[x + 3][y + 3] == token && cell[x + 4][y + 4] == token) {
					return true;
				}
			}
		}

		// Other diagonal wins?
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
