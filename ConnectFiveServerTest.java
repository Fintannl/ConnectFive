package com.fintan.connectfivetest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;

import org.junit.Test;

import com.fintan.connectfive.ConnectFiveServer;
import com.fintan.connectfive.GlobalConstants;


public class ConnectFiveServerTest {
	@Test
	  public void serverAcceptsConnection() throws IOException {
	    ConnectFiveServer.listen(GlobalConstants.INPUT_PORT); // creates the `serverSocket`

	    // create a `clientSocket` that will try to connect to a serverSocket
	    // that has the hostname 'localhost'
	    // and listens at port number 8080
	    try(Socket ableToConnect = new Socket("localhost", 8080)) {
	      assertTrue("Accepts connection when server in listening",
	                 ableToConnect.isConnected());
	      // close the `clientSocket`
	      ableToConnect.close();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    // close the 'serverSocket'
	    ConnectFiveServer.serverSocket.close();

	    try {
	      // now that `serverSocket` is closed
	      // try to connect another `clientSocket` to the same `serverSocket`
	      new Socket("localhost", 8080);
	      fail("Cannot connect if server socket is not listening");
	    } catch (Exception e) {
	      // assert that the exception is thrown and is the right exception
	      assertEquals("Connection refused", e.getMessage().trim());
	    }
	  }
	



}
