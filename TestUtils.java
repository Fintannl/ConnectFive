package com.fintan.connectfivetest;

public class TestUtils {
	static char cell[][] = new char[9][6];
	
	static char[][] setGameToDraw() {
	    char cell[][] = {{'X','O','X','O','X','O','X','O','X'},
	    				 {'O','X','O','X','O','X','O','X','O'},
	    				 {'X','O','X','O','X','O','X','O','X'},
	    				 {'X','O','X','O','X','O','X','O','X'},
	    				 {'X','O','X','O','X','O','X','O','X'},
	    				 {'O','X','O','X','O','X','O','X','O'}};  

		return cell;
	}
	
	static char[][] setVerticalToWin() {
	    char cell[][] = {{'X','O','X','O','X','O','X','O','X'},
						 {'X','O','X','O','X','O','X','O','X'},
						 {'X','O','X','O','X','O','X','O','X'},
						 {'X','O','X','O','X','O','X','O','X'},
						 {'X','O','X','O','X','O','X','O','X'},
						 {'X','O','X','O','X','O','X','O','X'}};  

		return cell;
	}
	
	static char[][] setHorizontalToWin() {
	    char cell[][] = {{'X','O','X','O','X','O','X','O','X'},
				 {'O','X','O','X','O','X','O','X','O'},
				 {'X','O','X','O','X','O','X','O','X'},
				 {'X','X','X','X','X','X','X','O','X'},
				 {'X','O','X','O','X','O','X','O','X'},
				 {'O','X','O','X','O','X','O','X','O'}}; 


		return cell;
	}
	
	
	static char[][] setForwardDiagonalToWin() { 
	    char cell[][] = {{'X','O','X','O','X','O','X','O','X'},
				 {'O','X','O','X','O','X','O','X','O'},
				 {'X','O','X','O','X','O','X','O','X'},
				 {'X','O','X','X','X','O','X','O','X'},
				 {'X','O','X','O','X','O','X','O','X'},
				 {'O','X','O','X','O','X','O','X','O'}};  

	    return cell;
	}
	
	static char[][] setReverseDiagonalToWin() {
	    char cell[][] = {{'X','O','X','O','X','O','X','O','X'},
				 		 {'O','X','O','X','O','X','O','X','O'},
				 		 {'X','O','X','O','X','O','X','O','X'},
				 		 {'X','O','X','X','X','O','X','O','X'},
				 		 {'X','O','X','O','X','O','X','O','X'},
				 		 {'O','X','O','X','O','X','O','X','O'}};  


		return cell;
	}	

}
