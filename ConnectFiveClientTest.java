package com.fintan.connectfivetest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fintan.connectfive.ConnectFiveClient;

public class ConnectFiveClientTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void clientCommunication() throws IOException {
	    ConnectFiveClient client = new ConnectFiveClient();
	    client.connectToServer();
	    int response = client.sendMessage(1);
	    assertEquals(1, response);
	    client.closeConnection();
	}
}
