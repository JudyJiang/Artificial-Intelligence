import java.util.*;

public class UCSSolver extends Solve{

	public UCSSolver(Board b, int method) {
		super(b, method);
		// TODO Auto-generated constructor stub
	}
	
	public int getMinPathIndex(LinkedList<State> queue){
		int index = 0, r_cost = 0;
		for(int i = 0; i < queue.size(); i++){
			int t_cost = queue.get(i).cor.getCost();
			if(r_cost < t_cost){
				r_cost = t_cost;
				index = i;
			}
		}
		return index;
	}
	
	public String ucsSolver(){
		long start_time, end_time;
		start_time = System.currentTimeMillis();
		System.out.println("Uniform");
		char[][] copyBoard;
		State start_state = new State(this.board.board, "", keeper);
		qu.push(start_state);
		visited.add(this.board.board);
		int count = 0;
		
		while(!qu.isEmpty()){
			int index = getMinPathIndex(qu);
			State current_state = qu.get(index);/* TODO add sort Function to get the smallest */
			copyBoard = current_state.board;
			so = current_state.path;
			int x = current_state.cor.x;
			int y = current_state.cor.y;
			
			qu.remove(index); /* TODO add remove index function */
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
						Coordinate new_keeper = new Coordinate(x + dx, y + dy);
						new_keeper.setCost(current_state.cor.getCost() + 2);
						visited.add(tempBoard);
						qu.add(new State(tempBoard, so + dirs.getDirections()[i].moveType[1],
								new_keeper));
						}
					}
				}
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
					Coordinate new_keeper = new Coordinate(x + dx, y + dy);
					new_keeper.setCost(current_state.cor.getCost() + 1);
					visited.add(tempBoard);
					qu.add(new State(tempBoard, so +dirs.getDirections()[i].moveType[0],
							new_keeper));
					}
				}
			}
			count++;
		}
		return "No Solution";
	}

}
