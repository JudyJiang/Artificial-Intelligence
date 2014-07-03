import java.util.*;

public class AI {
	public Board board;
	
	public Piece computerPiece;
	public Piece playerPiece;
	
	public static int Depth = 5;
	
	public long start_time = 0; //each time the computer starts time.
	public long period_time = 0;//thinking time for the computer. 
	
	public long end_time = 0;
	
	class Wrapped{
		int score;
		Position best_move;
		
		Wrapped(){
			score = 0;
			best_move = new Position(-1, -1);
		}
		
		Wrapped(int s, Position p){
			this.score = s;
			this.best_move = p;
		}
		
		public void setPosition(Position p){
			this.best_move = p;
		}
		
		public void setScore(int s){
			this.score = s;
		}
		
		public Position getPosition(){
			return this.best_move;
		}
		
		public int getScore(){
			return this.score;
		}
	}
	
	
	
	public AI(Piece c_piece){
		computerPiece = c_piece;
		if(computerPiece == Piece.X)
			playerPiece = Piece.O;
		else
			playerPiece = Piece.X;
	}
	
	
	/*
	 * Make a decision based on minimax pruning;
	 * 1. If there's one-move-win state, return that move;
	 * 2. Else, apply minimax, selectMax indicates current decision
	 * select - > true, means current in max decision
	 * select - > false, means current in min decision.
	 */
	public Position minimaxDecision(State current, int depth, int alpha, int beta, boolean selectMax){
		Set<State> sub_states = current.get_subStates();
		/*
		 * Allocate time slot for each potential state move. 
		 */
		long time_slot = this.period_time / sub_states.size();
		
		for(State one: sub_states){
			if(one.board.isWinnder(this.computerPiece)){
				Position win_pos = new Position(one.pos_x, one.pos_y);
				return win_pos;
			}
		}
		
		/*
		 * Otherwise do a minimax search.
		 */
		
        long current_last = System.currentTimeMillis();
		Wrapped decision = minimax(current, depth, alpha, beta, selectMax);
		Position next_pos = decision.getPosition();
		
		return next_pos;
	}
	
	
	
	
	public Wrapped minimax(State current, int depth, int alpha, int beta, boolean selectMax){
		/*
		 * If already reach the largest search depth or 
		 * current node is actually stop node (full / a winner)
		 */

		long temp = System.currentTimeMillis();
		/*if(temp >= this.end_time)
			System.out.println("This fast!");
		else
			System.out.println(temp);*/
		if(temp >= this.end_time || depth <= 0 || current.findWinner()){
			int evaluate = current.eval(computerPiece);
			Wrapped w = new Wrapped();
			w.setScore(evaluate);
			return w;
		}
		
		
		Wrapped best_move = new Wrapped();
		
		for(State one: current.get_subStates()){
			long current_last = System.currentTimeMillis();
			Wrapped w = minimax(one, depth - 1, alpha, beta, !selectMax);
			int curr_score = w.getScore(); 
			
			if(selectMax){
				if(curr_score > alpha){
					alpha = curr_score;
					best_move.setScore(alpha);
					Position p = new Position(one.pos_x, one.pos_y);
					best_move.setPosition(p);
				}
				
				if(alpha >= beta){
					break;
				}
			}
			else{
				if(curr_score < beta){
					beta = curr_score;
					Position p = new Position(one.pos_x, one.pos_y);
					best_move.setScore(beta);
					best_move.setPosition(p);
				}
				
				if(beta <= alpha){
					break;
				}
			}
		}
		
		int curr_eval = selectMax? alpha : beta;
		best_move.setScore(curr_eval);
		
		return best_move;
	}
	
	public Position getMove(Board b){
		Position next_move = null;
		State current = new State(b, computerPiece);
		this.end_time = this.start_time + this.period_time;
		next_move = minimaxDecision(current, Depth, -10001, 10001, false);
		
		return next_move;
	}
	
	public void setStartTime(long start){
		this.start_time = start;
	}
	
	public long getStartTime(){
		return this.start_time;
	}
	
	public void setPeriodTime(long period){
		this.period_time = period;
	}
	
	public long getPeriodTime(){
		return this.period_time;
	}
}
