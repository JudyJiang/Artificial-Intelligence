import java.util.*;

public class State {
	static class Data{
		int catch2;
		int uncatch2;
		int catch3;
		int uncatch3;
		int catch4;
		int uncatch4;
		
		Data(Board b, Piece p){
			if(b.win_chain > 3){
			this.catch2 = b.row(b.win_chain - 3, 0, p);
			this.uncatch2 = b.row(b.win_chain - 3, 1, p);
			}
			
			this.catch3 = b.row(b.win_chain - 2, 0, p);
			this.uncatch3 = b.row(b.win_chain - 2, 1, p);
			
			this.catch4 = b.row(b.win_chain - 1, 0, p);
			this.uncatch4 = b.row(b.win_chain - 1, 1, p);
		}
	}
	/*
	 * Overall information of a sub board (current board waiting to be placed)
	 */
	public Board board;
	public Piece currentPlayer;
	public Piece nextPlayer;

	/*
	 * Sub board with expected piece on position
	 */
	public int pos_x;
	public int pos_y;
	
	/*
	 * Each time before the computation, (The recursion loop) set up the time stamp
	 */
	public long initial_time_stamp;
	public long end_time_stamp = System.currentTimeMillis() + 6000 * 60;

	public State(Board b, Piece p) {
		board = b.cloneBoard();
		currentPlayer = p;
	}

	public State(Board b, Piece p, int x, int y) {
		this(b, p);
		this.pos_x = x;
		this.pos_y = y;
	}
	
	
	/* Time schedule */
	public void set_current_time_stamp(long initial_time){
		this.initial_time_stamp = initial_time;
	}
	 
	
	public void set_end_time_stamp(long period){
		this.end_time_stamp = this.initial_time_stamp + period;
	}
	
	public long get_current_time_stamp(){
		return System.currentTimeMillis();
	}
	/* Time Schedule */ 
	
	

	public Set<State> get_subStates() {
		Set<State> subStates = new HashSet<State>();
		Set<Position> possiblePositions = new HashSet<Position>();
		Piece nextPlayer = this.nextPlayer();
        possiblePositions = getPossibleMoves();
		for (Position pos : possiblePositions) {
			Board sub_board = this.board.cloneBoard();
			boolean added = sub_board.addPiece(pos.pos_x, pos.pos_y,
					currentPlayer);

			if (added) {
				State sub_state = new State(sub_board, nextPlayer, pos.pos_x,
						pos.pos_y);
				subStates.add(sub_state);
			}
		}
		return subStates;
	}

	/*
	 * Get the next player of this game(For use in get_subStates to exchange the
	 * player state
	 */
	public Piece nextPlayer() {
		if (this.currentPlayer == Piece.X)
			return Piece.O;
		else
			return Piece.X;
	}

	/*
	 * Find the list of positions available for currentPlayer 1. The position is
	 * empty; 2. It's not a corner position. (Corner means
	 */
	public Set<Position> getPossibleMoves() {
		Set<Position> possibles = new HashSet<Position>();
		for (int i = 0; i < this.board.board_size; i++) {
			for (int j = 0; j < this.board.board_size; j++) {
				if (this.board.checkValid(i, j) && (!isCorner(i, j)))
					possibles.add(new Position(i, j));
			}
		}
		return possibles;
	}

	public boolean isCorner(int x, int y) {
		boolean corner = false;
		if (this.board.checkValid(x - 1, y - 1)
				|| this.board.checkValid(x - 1, y)
				|| this.board.checkValid(x, y + 1)
				|| this.board.checkValid(x, y - 1)
				|| this.board.checkValid(x, y + 1)
				|| this.board.checkValid(x + 1, y - 1)
				|| this.board.checkValid(x + 1, y)
				|| this.board.checkValid(x + 1, y + 1))
			corner = false;

		else
			corner = true;
		return corner;
	}

	public boolean findWinner() {
		boolean win = false;
		if (this.board.isFull())
			win = true;
		else if (this.board.isWinnder(currentPlayer)
				|| this.board.isWinnder(this.nextPlayer()))
			win = true;

		return win;
	}

	/*
	 * The evaluation function takes the heuristic into consideration Heuristic
	 * takes into account which situation can lead to the best score for current
	 * player.
	 * 
	 * For any chain (since more than 5, the strategy can be found in a research
	 * paper online
	 * http://library.thinkquest.org/18242/data/resources/gomoku.pdf A typical
	 * AI strategy for searching using "continues four", "broken 4" or
	 * "continues 3" or "broken 3" Here update to any length chain.
	 */
	public int eval(Piece computerPiece) {
		Piece next;
		if(computerPiece == Piece.O)
			next = Piece.X;
		else
			next = Piece.O;
		
		Data data1 = new Data(this.board, computerPiece);
		Data data2 = new Data(this.board, next);
		int score = 0;
		
		if(data2.uncatch4 > 0)
			return -10000;
		if(data1.uncatch4 > 0)
			return 10000;
		
		score += data1.catch2 * 5;
		score -= data2.catch2 * 5;
		
		score += data1.uncatch2 * 10;
		score -= data2.uncatch2 * 10;
		
		score += data1.catch3 * 20;
		score -= data2.catch3 * 30;
		
		score += data1.uncatch3 * 100;
		score -= data2.uncatch3 * 120;
		
		score += data1.catch4 * 500;
		score -= data2.catch4 * 500;
		
		return Math.max(-10000, Math.min(10000, score));
	}
	

}
