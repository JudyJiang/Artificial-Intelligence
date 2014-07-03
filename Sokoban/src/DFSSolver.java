import java.util.ArrayList;
import java.util.LinkedList;

public class DFSSolver extends Solve{
	String solution = "";
	String str = "";
	public DFSSolver(Board b, int method) {
		super(b, method);
		for(int i = 0; i < b.board.length; i++)
			for(int j = 0; j < b.board[i].length; j++){
				if(b.board[i][j] == '$')
					b.board[i][j] = '*';
			}
		//printBoard(b.board);
	}

	public String dfsSolver(){
		System.out.println("DFS Solver");
		long start_time, end_time;
		start_time = System.currentTimeMillis();
		char[][] copyBoard;
		qu.push(new State(this.board.board, "", keeper));
		visited.add(this.board.board);
		int count = 0;
		while(!qu.isEmpty()){
			State current_state = qu.peekLast();
			copyBoard = current_state.board;
			so = current_state.path;
			int x = current_state.cor.x;
			int y = current_state.cor.y;
			qu.removeLast();
			number_of_nodes++;
			for(int i = 0; i < 4; i++){
				//number_of_nodes++;
				int dx = dirs.getDirections()[i].direction.x;
				int dy = dirs.getDirections()[i].direction.y;
				char[][] tempBoard = cloneBoard(copyBoard);
				
				
				if(tempBoard[x + dx][y + dy] == '$' || tempBoard[x + dx][y + dy] == '*'){
					if(push(x, y, dx, dy, tempBoard)){
						if(isVisited(visited, tempBoard))
							previous_nodes++;
						else{
							if(isSolved(tempBoard)){
								fringe_nodes = qu.size();
								explored_list = visited.size();
								end_time = System.currentTimeMillis();
								taken_time = end_time - start_time;
								return so + dirs.getDirections()[i].moveType[1];
							}
							qu.add(new State(tempBoard, so + dirs.getDirections()[i].moveType[1],
									new Coordinate(x + dx, y + dy)));
							visited.add(tempBoard);
						}
					}
				}//meet box
				
				else if(move(x, y, dx, dy, tempBoard)){
					if(isVisited(visited, tempBoard))
						previous_nodes++;
					else{
						if(isSolved(tempBoard)){
							fringe_nodes = qu.size();
							explored_list = visited.size();
							end_time = System.currentTimeMillis();
							taken_time = end_time - start_time;
							return so + dirs.getDirections()[i].moveType[0];
						}
						qu.add(new State(tempBoard, so + dirs.getDirections()[i].moveType[0],
								new Coordinate(x + dx, y + dy)));
						visited.add(tempBoard);
					}
				}
			}// four directions;
	    count++;
		}//while the queue is still full
		
		
		return "No Solutions";
	}
}

