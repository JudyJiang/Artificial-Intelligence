import java.util.*;

public class Solve {
	ArrayList<char[][]> visited = new ArrayList<char[][]>();
	
	Board board;
	Coordinate keeper;
	LinkedList<State> qu = new LinkedList<State>();
	ArrayList<State> start_visited = new ArrayList<State>();
	
	Direction dirs;
	static String so = "";
	int para = 0;
	String heu = "";
	
	BFSSolver bfs;
	DFSSolver dfs;
	UCSSolver ucs;
	GreedyBestFirst gbf;
	Search as;
	
	//boolean is_statistics = false;
	static int  number_of_nodes = 0;
	static int previous_nodes = 0;
	static int fringe_nodes = 0;
	static int explored_list = 0;
	static long taken_time = 0;
	
	
	public Solve(Board b, int method){
		number_of_nodes = 0;
		previous_nodes = 0;
		fringe_nodes = 0;
		explored_list = 0;
		taken_time = 0;
		board = b;
		para = method;
		keeper = board.keeper;
		dirs = new Direction();
	}
	
	public Solve(Board b, int method, String heu_method){
		number_of_nodes = 0;
		previous_nodes = 0;
		fringe_nodes = 0;
		explored_list = 0;
		taken_time = 0;
		board = b;
		para = method;
		heu = heu_method;
		keeper = board.keeper;
		dirs = new Direction();
		
	}
	
	public int boxInGoals(char[][] t_board){
		int count = 0;
		for(Coordinate c: board.goals){
			if(t_board[c.x][c.y] == '*')
				count++;
		}
		return count;
	}
	
	//minimum number of steps to reach a goal

	
	public String begin () throws Exception {
		System.out.println("Original Puzzle");
		printBoard(board.board);
		switch(para){
		case 1:
			bfs = new BFSSolver(board, para);
			return bfs.bfsSolver();
		case 2:
			dfs = new DFSSolver(board, para);
			return dfs.dfsSolver();
		case 3:
			ucs = new UCSSolver(board, para);
			return ucs.ucsSolver();
			
		case 4:
			//System.out.println("Informed Search");
			if(heu.equals("heu1"))
				gbf = new GreedyBestFirst(board, para, heu);
			else if(heu.equals("heu2"))
				gbf = new GreedyBestFirst(board, para, heu);
			else
				throw new Exception("Illegal Argument\n");
			return gbf.gbfSolver();
			
		case 5:
			if(heu.equals("heu1"))
				as = new Search(board, para, heu);
			else if(heu.equals("heu2"))
				as = new Search(board, para, heu);
			return as.AStarSolver();
		default:
			return null;
		}
	}
	
	/*
	 * To see if the current state of the tempBoard is the
	 * goal state: If all boxes are on goal positions
	 */
	public boolean isSolved(char[][] tempBoard){
		for(Coordinate c: board.goals){
			int t_x = c.x;
			int t_y = c.y;
			if(tempBoard[t_x][t_y] != '*')
				return false;
		}
		System.out.println("Is Solved");
		printBoard(tempBoard);
		return true;
	}
	
	/*
	 * If the keeper can push the box to the direction. 
	 * The keeper can push the box only if the position after the push is not available, not a wall
	 * If push is done, the keeper move one step forward, the original position is blank now.
	 */
	public boolean push(int x, int y, int dx, int dy, char[][] b){
		if(b[x + 2 * dx][y + 2 * dy] == '#' || b[x + 2 * dx][y + 2 *dy] == '$'
				||b[x + 2 * dx][y + 2 * dy] == '*')
			return false;
		b[x][y] = ' ';
		b[x + 2 * dx][y + 2 * dy] = '*';
		b[x + dx][y + dy] = '@';
		return true;
	}
	
	/*
	 * If the keeper can move to the (dx,dy) direction. 
	 * A keeper can move if current position adds dx, dy is now available, not a wall.
	 */
	public boolean move(int x, int y, int dx, int dy, char[][] b){
		if(b[x + dx][y + dy] == '#' || b[x + dx][y + dy] == '*' 
				|| b[x + dx][y + dy] == '$')
			return false;
		
		b[x][y] = ' ';
		b[x + dx][y + dy] = '@';
		return true;
	}
	
	
	/*
	 * Print the current Board
	 */
	public void printBoard(char[][] copy){
		for(int i = 0; i < copy.length; i++){
			for(int j = 0; j <copy[i].length; j++)
				System.out.print(copy[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	
	
	/*
	 * print statistics data, number of nodes.
	 */
	public void printStatistics(){
		System.out.print("Number of Nodes generated is: " + number_of_nodes + "\n" + 
	                     "Number of Nodes generated previoiusly is: " + previous_nodes + "\n" + 
	                     "Number of Nodes on fringe is: " + fringe_nodes + "\n" + 
	                     "Number of Nodes explored is: " + explored_list + "\n" +
	                     "Time taken is:" + (double)taken_time / 1000 + " secons " +"\n");
		  
	}
	
	public boolean compare(char[][] c1, char[][] c2){
		for(int i = 0; i < c1.length; i++){
			for(int j = 0; j < c1[0].length; j++)
				if(c1[i][j] != c2[i][j])
					return false;
		}
		return true;
	}
	
	
	/*
	 * Check if the current board state is already in the list of visited state part. 
	 * If not, can go expend the current node.
	 * If yes, ignore the current node.
	 */
	public boolean isVisited(ArrayList<char[][]> visited, char[][] temp){
		for(char[][] c: visited){
			if(compare(c, temp))
				return true;
		}
		
		return false;
	}// finish all travel than return visited...yours just conpare one and return different.
	
	
	public int visitedIndex(ArrayList<char[][]> visited, char[][] temp){
		for(int i = 0; i < visited.size(); i++){
			if(compare(visited.get(i), temp))
				return i;
		}
		return -1;
	}
	
	
	
	public char[][] cloneBoard(char[][] b){
		char[][] copy = new char[b.length][b[0].length];
		for(int i = 0; i < b.length; i++)
			for(int j = 0; j < b[0].length; j++)
				copy[i][j] = b[i][j];
		return copy;
	}
	
	
	 
}
