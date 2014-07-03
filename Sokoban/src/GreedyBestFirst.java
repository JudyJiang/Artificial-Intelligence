import java.util.*;
public class GreedyBestFirst extends Solve{
	ArrayList<Coordinate> goals = new ArrayList<Coordinate>();
	public GreedyBestFirst(Board b, int method, String heu) throws Exception {
		super(b, method, heu);
		//printBoard(b.board);
		goals = b.goals;
		if(keeper != null)
			boxInGoals(b.board);
		else{
			System.out.println("No Keeper");
			System.exit(1);
		}
	}
	
	
	/* Here the Coordinate cost is the number of current_state's boxes in goal*/
	/*
	 * Heu one;
	 */
	public int HeuristicOne(LinkedList<State> queue){
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
	
	
	/*
	 * Heu two; Return each goals' distances to boxes.
	 * get Goal distances. 
	 */
	public int HeuristicTwo(LinkedList<State> queue){
		
		int index = 0;
		int r_cost = queue.get(0).cor.getCost();
		for(int i = 1; i < queue.size(); i++){
			int t_cost = queue.get(i).cor.getCost();
			if(r_cost > t_cost){
				r_cost = t_cost;
				index = i;
			}
		}
		return index;
	}
	
	
	public String gbfSolver() throws Exception{
		long start_time, end_time;
		start_time = System.currentTimeMillis();
		System.out.println("Greedy Search");
		char[][] copyBoard;
		State start_state = new State(this.board.board, "", keeper);
		qu.add(start_state);
		visited.add(this.board.board);
		int count = 0;
		int index = -1;
		
		while(!qu.isEmpty()){
			if(heu.equals("heu1"))
				index = HeuristicOne(qu);
			else if(heu.equals("heu2"))
				index = HeuristicTwo(qu);
			else
				throw new Exception("Wrong Argument\n");
			
			State current_state = qu.get(index);
			copyBoard = current_state.board;
			so = current_state.path;
			int x = current_state.cor.x;
			int y = current_state.cor.y;
			
			qu.remove(index);
			number_of_nodes++;
			for(int i = 0; i < 4; i++){
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
						State add_state = new State(tempBoard, so + dirs.getDirections()[i].moveType[1], new_keeper);
						if(heu.equals("heu1"))
							add_state.cor.setCost(boxInGoals(tempBoard));
						else if(heu.equals("heu2")){
							add_state.cor.setCost(add_state.getStepsFromGoal(tempBoard, goals));
						}
						visited.add(tempBoard);
						qu.add(add_state);
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
					State add_state = new State(tempBoard, so + dirs.getDirections()[i].moveType[0], new_keeper);
					
					if(heu.equals("heu1"))
						add_state.cor.setCost(boxInGoals(tempBoard));
					else if(heu.equals("heu2")){
						add_state.cor.setCost(add_state.getStepsFromGoal(tempBoard, goals));
					}
					visited.add(tempBoard);
					qu.add(add_state);
					}
				}
			}
			count++;
		}
		return "No Solution";
	}

}
