import java.util.*;

public class Board {
	char[][] board; // Coordinate[][] board??no State[][] board;
	Coordinate keeper = null;
	ArrayList<Coordinate> boxes = new ArrayList<Coordinate>();
	ArrayList<Coordinate> goals = new ArrayList<Coordinate>();
	ArrayList<Coordinate> walls = new ArrayList<Coordinate>();
	
	
	/*
	 * Initialization of Board, keep records of positions of boxes, keeper
	 * and goals..
	 */
	public Board(int row, int column, ArrayList<String> map){
		board = new char[row][column];
		for(int i = 1; i < map.size(); i++){
			for(int j = 0; j < map.get(i).length(); j++){
				char c = map.get(i).charAt(j);
				board[i - 1][j] = c;
				
				switch(c){
				case ' ':
					break;
					
				case '#':
					walls.add(new Coordinate(i - 1, j));
					break;
					
				case '$':
					boxes.add(new Coordinate(i - 1, j));
					break;
					
				case '.':
					goals.add(new Coordinate(i - 1, j));
					break;
					
				case '*':
					boxes.add(new Coordinate(i - 1, j));
					goals.add(new Coordinate(i - 1, j));
					break;
					
				case '@':
					if(keeper != null){
						throw new IllegalArgumentException();
					}
					keeper = new Coordinate(i - 1, j);
					break;
					
				case '+':
					if(keeper != null)
						throw new IllegalArgumentException();
					keeper = new Coordinate(i - 1, j);
					goals.add(new Coordinate(i - 1,j));
					break;
					
				default:
					throw new IllegalArgumentException();	
				}		
			}
		}
	}//end of board initialization.
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public char[][] clone(){
		char[][] copy = new char[board.length][board[0].length];
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++)
				copy[i][j] = board[i][j];
		}
		return copy;
	}
	
	
	/*
	 * Get the maximum of column size;
	 */
	public int getMaxColumnSize(){
		int count = 0;
		for(int i = 0; i < board.length; i++){
			if(count < board[i].length)
				count = board[i].length;
		}
		return count;
	}
}
