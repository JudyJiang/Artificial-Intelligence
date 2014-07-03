import java.util.*;

public class Search extends Solve{
	
	public ArrayList<Coordinate> goals = new ArrayList<Coordinate>();
	
	public Search(Board b, int method, String heu_method) {
		super(b, method, heu_method);
		goals = b.goals;
		if(keeper != null)
			boxInGoals(b.board);
		else{
			System.out.println("No Keeper");
			System.exit(1);
		}
	}
	
	
	public int HeuristicOne(LinkedList<State> qu){
		int index = 0;
		int r_cost = qu.get(0).cor.getSteps();
		for(int i = 1; i < qu.size(); i++){
			int t_cost = qu.get(i).cor.getSteps();
			if(r_cost > t_cost){
				r_cost = t_cost;
				index = i;
			}
		}
		return index;
	}
	
	
	public int HeuristicTwo(LinkedList<State> queue){
		int index = 0;
		int r_cost = queue.get(0).cor.getCost();
		for(int i = 1; i < queue.size(); i++){
			int t_cost = queue.get(i).cor.getCost();
			if(r_cost < t_cost){
				r_cost = t_cost;
				index = i;
			}
		}
		return index;
	}
	
	public String AStarSolver() throws Exception{
		long start_time, end_time;
		start_time = System.currentTimeMillis();
		System.out.println("A Star");
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
						int visited_index = visitedIndex(visited, tempBoard);
						if(visited_index != -1){
							previous_nodes++;
		// // // // // //   int visited_cost = 
						}
						else{
						if(isSolved(tempBoard)){
							fringe_nodes = qu.size();
							explored_list = visited.size();
							end_time = System.currentTimeMillis();
							taken_time = end_time - start_time;
							return so + dirs.getDirections()[i].moveType[1];
						}
						Coordinate new_keeper = new Coordinate(x + dx, y + dy);
						new_keeper.setCost(current_state.cor.getCost() + 1);
						State add_state = new State(tempBoard, so + dirs.getDirections()[i].moveType[1], new_keeper);
						if(heu.equals("heu1")){
							int steps = add_state.getStepsFromGoal(tempBoard, goals) + add_state.cor.getCost();
							add_state.cor.setSteps(steps);
						}
						else if(heu.equals("heu2")){
							int steps = add_state.cor.getCost() + boxInGoals(tempBoard);
							add_state.cor.setSteps(steps);
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
					new_keeper.setCost(current_state.cor.getCost() + 1);
					State add_state = new State(tempBoard, so + dirs.getDirections()[i].moveType[0], new_keeper);
					
					if(heu.equals("heu1")){
						int steps = add_state.getStepsFromGoal(tempBoard, goals) + add_state.cor.getCost();
						add_state.cor.setSteps(steps);
					}
					else if(heu.equals("heu2")){
						int steps = add_state.cor.getCost() + boxInGoals(tempBoard);
						add_state.cor.setSteps(steps);
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
