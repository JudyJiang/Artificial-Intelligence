import java.util.*;

public class State {
	char[][] board;
	String path = "";
	Coordinate cor;
	int[][] points;
	
	public State(char[][] new_board, String _path, Coordinate new_cor){
		board = new_board;
		path = _path;
		cor = new_cor;
	//	int column = getCol();
		points = new int[board.length][getCol()];
		for(int i = 0; i < board.length; i++)
			for(int j = 0;j < board[i].length; j++)
				points[i][j] = 0;
	}
	
	public int getCol(){
		int col = 0; 
		for(int i = 0; i < board.length; i++)
			if(col < board[i].length)
				col = board[i].length;
		return col;
	}
	
	/*
	 * Judge whether or not current position is a goal position.
	 */
	public boolean isGoal(ArrayList<Coordinate> goals, int x, int y){
		for(Coordinate c: goals){
			if(c.x == x && c.y == y)
				return true;
		}
		return false;
	}
	
	/*
	 * Get sum of how many steps needed to reach all goal positions. 
	 */
	public int getGoalPoints(ArrayList<Coordinate> goals){
		int sum = 0;
		for(Coordinate c: goals){
			/*System.out.print("Goals " + c.x + " " + c.y + " ");
			System.out.print("Goal Points " + points[c.x][c.y] + " "); */
			sum += points[c.x][c.y];
		}
		//System.out.println("Total Goal Points " + sum);
		return sum;
	}
	
	/*
	 * Calculate how many steps there're from current position to the each of the goal position
	 * The "box" is treated as open space here.
	 */
	public int getStepsFromGoal(char[][] board, ArrayList<Coordinate> goals){
		int steps = 0;
		Direction dirs = new Direction();
		LinkedList<Coordinate> queue = new LinkedList<Coordinate>();
		queue.add(cor);
		
		while(!queue.isEmpty()){
			Coordinate p = queue.removeFirst();
			int x = p.x;
			int y = p.y;
			
			
			for(int i = 0; i < 4; i++){
				int dx = dirs.getDirections()[i].direction.x;
				int dy = dirs.getDirections()[i].direction.y;
				
				
				if(board[x + dx][y + dy] != '#' && points[x + dx][y + dy] == 0){
					queue.add(new Coordinate(x + dx, y + dy));
					points[x + dx][y + dy] = points[x][y] + 1;
				}
			}
		}points[cor.x][cor.y] = 0;
		
		return getGoalPoints(goals);
	}
}
