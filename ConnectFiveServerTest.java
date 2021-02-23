
package com.fintan.connectfive;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;
public class ConnectFiveServerTest {



	@Test
	  public void serverAcceptsConnection() throws IOException {
		ConnectFiveServer.listen(1234); // creates the `serverSocket`

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

	public class MockSocket extends Socket {
		  // a mockSocket constructor does not need hostname and port number
		  public MockSocket() {}

		  // return an InputStream with a dummy request
		  public InputStream getInputStream() {
		    return new ByteArrayInputStream("GET / HTTP/1.1\nHost: localhost".getBytes());
		  }

		  // coming up next! :)
		  public OutputStream getOutputStream() {
		    return new OutputStream() {
		      @Override
		      public void write(int b) throws IOException {

		      }
		    };
		  }
		}
	

	
	
	
}

