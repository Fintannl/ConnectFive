package com.fintan.connectfivetest;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.fintan.connectfive.SessionHandler;


public class SessionHandlerTest {

	
	@Test
	public void testGameIsADraw() {		
		char[][] cell = TestUtils.setGameToDraw();
		assertTrue(SessionHandler.isFull(cell));
	}

	
	@Test
	public void testGameIsWonVertical() {		
		char[][] cell = TestUtils.setVerticalToWin();
		assertTrue(SessionHandler.isFull(cell));
	}
	
	@Test
	public void testGameIsWonHorizontal() {		
		char[][] cell = TestUtils.setHorizontalToWin();
		assertTrue(SessionHandler.isFull(cell));
	}
	
	@Test
	public void testGameIsWonForwardDiagonal() {		
		char[][] cell = TestUtils.setForwardDiagonalToWin();
		assertTrue(SessionHandler.isFull(cell));
	}
	
	@Test
	public void testGameIsWonReverseDiagonal() {		
		char[][] cell = TestUtils.setReverseDiagonalToWin();
		assertTrue(SessionHandler.isFull(cell));
	}
}