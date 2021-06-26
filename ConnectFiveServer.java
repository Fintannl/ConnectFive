package com.fintan.connectfive;

import java.io.*;
import java.net.*;

public class ConnectFiveServer {

	static int port = GlobalConstants.INPUT_PORT;
	public static ServerSocket serverSocket;
	public static Object toPlayer1;


	public static void listen(int port) throws IOException {

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

				SessionHandler task = new SessionHandler(player1, player2);
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
