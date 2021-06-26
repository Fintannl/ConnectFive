package com.fintan.connectfive;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConnectFiveClient implements Runnable {

	static char gameData[][] = new char[9][6];
	static int nextColumn = 0;
	static String player1Name = "";
	static String player2Name = "";
	DataInputStream fromServer;
	DataOutputStream toServer;
	Socket socket;

	private boolean myTurn = false;
	private char myToken = 'X';
	private char otherToken = 'O';
	private int rowSelected;
	private int columnSelected;
	private int player;
	private boolean continueToPlay = true;
	private String host = "localhost";
	private String currentPlayerName = "";

	public void connectToServer() {

		try {
			socket = new Socket(host, GlobalConstants.INPUT_PORT);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}

		Thread thread = new Thread(this);
		thread.start();
	}
    public int sendMessage(int n) throws IOException {
        toServer.writeInt(n);
        int resp = fromServer.readInt();
        return resp;
    }
    
    public void closeConnection() throws IOException {
    	fromServer.close();
    	toServer.close(); 	
    }

	public void run() {
		try {
			player = fromServer.readInt();
			enterPlayerName();
			while (continueToPlay) {
				if (player == GlobalConstants.PLAYER1) {
					currentPlayerName = player1Name;
					refresh();
					enterColumn();
					playerMoveCheck(columnSelected);
					sendMove();
					receiveInfoFromServer();
				} else {
					currentPlayerName = player2Name;
					receiveInfoFromServer();
					refresh();
					enterColumn();
					playerMoveCheck(columnSelected);
					sendMove();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			
			
		}
	}

	public void enterPlayerName() {
		if (player == GlobalConstants.PLAYER1) {
			myToken = 'X';
			otherToken = 'O';
			try (Scanner input = new Scanner(System.in)) {
				System.out.println("Hello Player1, Please enter your name");
				player1Name = input.nextLine();
				System.out.println("Welcome :" + player1Name + ", Waiting for player 2 to join");
			}
			try {
				fromServer.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			} // waiting for the other player to join
			myTurn = true;
		} else if (player == GlobalConstants.PLAYER2) {
			myToken = 'O';
			otherToken = 'X';
			try (Scanner input = new Scanner(System.in)) {
				System.out.println("Hello Player2, Please enter your name");
				player2Name = input.nextLine();
				System.out.println(player2Name + ", has joined the game");
			}

		}

	}

	public void refresh() throws IOException {
		for (int rows = 0; rows < 6; rows++) {

			for (int cols = 0; cols < 9; cols++) {
				System.out.print(" [");
				System.out.print(gameData[cols][rows]);
				System.out.print("]");
			}
			System.out.println("");
		}
	}

	/** Wait for the player to make a move */
	private void enterColumn() throws InterruptedException {

		try (Scanner scan = new Scanner(System.in)) {
			System.out.print("It’s your turn " + currentPlayerName + ", please enter column (1-9):");
			int columnSelected = scan.nextInt();
			if (columnSelected < 1 || columnSelected > 9) {
				System.out.println("Invalid Value, please try again");
				System.out.print("It’s your turn " + currentPlayerName + ", please enter column (1-9):");
				columnSelected = scan.nextInt();
			}
		}
	}

	/** Send this player's move to the server */
	private void sendMove() throws IOException {
		toServer.writeInt(rowSelected); // Send the selected row
		toServer.writeInt(columnSelected); // Send the selected column
	}

	/** Receive info from the server */
	private void receiveInfoFromServer() throws IOException {
		// Receive game status
		int status = fromServer.readInt();

		if (status == GlobalConstants.PLAYER1_WON) {
			// Player 1 won, stop playing
			continueToPlay = false;
			if (myToken == 'X') {
				System.out.println("I won!");
			} else if (myToken == 'O') {
				System.out.println(player1Name + " has won!");
				receiveMove();
			}
		} else if (status == GlobalConstants.PLAYER2_WON) {
			// Player 2 won, stop playing
			continueToPlay = false;
			if (myToken == 'O') {
				System.out.println("I won!");
			} else if (myToken == 'X') {
				System.out.println(player2Name + " has won!");
				receiveMove();
			}
		} else if (status == GlobalConstants.DRAW) {
			// No winner, game is over
			continueToPlay = false;
			System.out.println("Game is over, no winner!");

			if (myToken == 'O') {
				receiveMove();
			}
		} else {
			receiveMove();
			System.out.println("My turn");
			myTurn = true;
		}
	}

	private void receiveMove() throws IOException {
		// Get the other player's move
		int row = fromServer.readInt();
		int column = fromServer.readInt();

		gameData[row][column] = otherToken;

	}

	private void playerMoveCheck(int column) {
		int r = -1;
		for (int x = 5; x >= 0; x--) {
			if (gameData[x][column] == ' ') {
				r = x;
				break;
			}
		}
		// If location is not occupied and the player has the turn
		if ((r != -1) && myTurn) {
			gameData[r][column] = myToken;
			myTurn = false;
			rowSelected = r;
			columnSelected = column;
			System.out.println("Waiting for the other player to move");
		} else {
			System.out.println("Invalid move, please try again");
			try {
				enterColumn();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * main method
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		ConnectFiveClient letsGetThisGameStarted = new ConnectFiveClient();
		if (args.length == 1)
			letsGetThisGameStarted.host = args[0];
		letsGetThisGameStarted.connectToServer();
	}
}
