public class Board {

	public int board_size;
	public int win_chain;

	boolean win = false;

	// public static enum Piece {EMPTY, X, O};

	public Piece[][] board;

	/*
	 * By default, 5 * 5;
	 */
	public Board() {
		// TODO Auto-generated constructor stub

	}

	public Board(int size, int chain) {
		this.board_size = size;
		this.win_chain = chain;
		board = new Piece[size][size];
		initialize();
	}

	public void initialize() {
		for (int i = 0; i < board_size; i++)
			for (int j = 0; j < board_size; j++)
				board[i][j] = Piece.EMPTY;
	}

	
	/*
	 * Check whether or not the input position is valid
	 * (Dimension valid and whether or not it's empty)
	 */
	public boolean checkValid(int x, int y) {
		if(x < 0 || y < 0 || x > board_size - 1 || y > board_size - 1)
			return false;
		else if(board[x][y] != Piece.EMPTY)
			return false;
		else
			return true;
	}

	/*
	 * Place the piece on the board if the board position is empty. Else return
	 * false*****Should lose the game if place an inappropriate position?
	 */
	public boolean addPiece(int x, int y, Piece cur) {
		if (!checkValid(x, y))
			return false;
		if (board[x][y] == Piece.O || board[x][y] == Piece.X)
			return false;
		else {
			board[x][y] = cur;
			return true;
		}
	}

	/*
	 * IF the board is full of pieces.
	 */
	public boolean isFull() {
		for (int i = 0; i < board_size; i++)
			for (int j = 0; j < board_size; j++)
				if (board[i][j] == Piece.EMPTY)
					return false;

		return true;
	}
	
	
	/*
	 * If the board is Empty
	 */
	public boolean isEmpty(){
		for(int i = 0; i < board_size; i++)
			for(int j = 0; j < board_size; j++){
				if(board[i][j] != Piece.EMPTY)
					return false;
			}
		return true;
	}

	public void printBoard() {
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if (board[i][j] == Piece.EMPTY)
					System.out.print('.' + " ");
				else
					System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		/*for(int i = 0; i < board_size; i++)
			System.out.print(" " + i + " ");
		System.out.println();
		for(int i = 0; i < board_size; i++){
			System.out.print(i + " ");
			for(int j = 0; j < board_size; j++){
				if(board[i][j] == Piece.EMPTY)
					System.out.print('.' + " ");
				else
					System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}*/
	}

	/*
	 * Search winner in a row XXXXX
	 */

	public boolean rowWin(Piece p) {
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j <= board_size - win_chain; j++) {
				int k = 0;
				for (k = 0; k < win_chain; k++) {
					if (board[i][j + k] != p) {
						win = false;
						break;
					}
				}
				if (k == win_chain) {
					win = true;
					break;
				}
			}
			if (win == true)
				break;
		}
		return win;
	}

	/*
	 * Search winner in a column
	 */
	public boolean columnWin(Piece p) {
		for (int i = 0; i <= board_size - win_chain; i++) {
			for (int j = 0; j < board_size; j++) {
				int k = 0;
				for (k = 0; k < win_chain; k++) {
					if (board[i + k][j] != p) {
						win = false;
						break;
					}
				}
				if (k == win_chain) {
					win = true;
					break;
				}
			}
			if (win == true)
				break;
		}
		return win;
	}

	/*
	 * Search winner in a diagonal, bio-direction
	 */
	public boolean diagonalWin(Piece p) {
		for (int i = 0; i <= board_size - win_chain; i++) {
			for (int j = 0; j <= board_size - win_chain; j++) {
				int k = 0;
				for (k = 0; k < win_chain; k++) {
					if (board[i + k][j + k] != p) {
						win = false;
						break;
					}
				}
				if (k == win_chain) {
					win = true;
					break;
				}
			}
			if (win == true)
				break;
		}

		/*
		 * If there's already a winner in one dia-direction return true, stop
		 * searching.
		 */
		if (win == true)
			return win;

		else {
			for (int i = 0; i <= board_size - win_chain; i++) {
				for (int j = win_chain - 1; j < board_size; j++) {
					int k = 0;
					for (k = 0; k < win_chain; k++) {
						if (board[i + k][j - k] != p) {
							win = false;
							break;
						}
					}
					if (k == win_chain) {
						win = true;
						break;
					}
				}
				if (win == true)
					break;
			}
		}
		return win;
	}

	/*
	 * Search the board to find it there's a "Piece p" tyep winner
	 */
	public boolean isWinnder(Piece p) {
		if (rowWin(p))
			win = true;
		else if (columnWin(p))
			win = true;
		else if (diagonalWin(p))
			win = true;
		return win;
	}

	public Board cloneBoard() {
		Board clone_board = new Board(this.board_size, this.win_chain);
		for (int i = 0; i < this.board_size; i++)
			for (int j = 0; j < this.board_size; j++)
				clone_board.board[i][j] = this.board[i][j];

		return clone_board;
	}
	
	public int row(int continues, int num, Piece p){
		int count = 0;
		for(int i = 0; i < this.board_size; i++){
			for(int j = 0; j < this.board_size; j++){
				if((this.board[i][j] == p) && aroundCenter(i, j, continues, num) != 0)
					count++;
			}
		}
		return count;
	}
	
	public int aroundCenter(int row, int col, int range, int block){
		int count = 0;
		int length = this.board_size;
		
		Piece current = this.board[row][col];
		if(current == Piece.EMPTY){
			count = 0;
			return count;
		}
		Piece opposite = (current == Piece.X) ? Piece.O : Piece.X;
		
		int rangeFactor, blockFactor;
		
		/*
		 * Check Horizontal.
		 */
		if(col + range <= length){
			rangeFactor = 1;
			for(int i = col + 1; (i < col + range) && (rangeFactor != 0); i++){
				if(this.board[row][i] != current)
					rangeFactor = 0;
			}
			
			if(rangeFactor != 0){
				blockFactor = 0;
				
				if(col == 0)
					blockFactor++;
				
				else if(col >= 1 && this.board[row][col - 1] == opposite)
					blockFactor++;
				
				else if(col + range == length)
					blockFactor++;
				
				else if(col + range < length && this.board[row][col + range] == opposite)
					blockFactor++;
				
				if(blockFactor <= block)
					count++;
			}
		}
		/*
		 * Finish check Horizontal. 
		 */
		
		
		/*
		 * Check vertical
		 */
		if(row + range <= length){
			rangeFactor = 1;
			for(int i = row + 1; (i < row + range) && (rangeFactor != 0); i++)
				if(this.board[i][col] != current)
					rangeFactor = 0;
			
			if(rangeFactor != 0){
				blockFactor = 0;
				
				if(row == 0)
					blockFactor++;
				
				if(row >= 1 && this.board[row - 1][col] == opposite)
					blockFactor++;
				
				if(row + range == length)
					blockFactor++;
				
				if(row + range < length && this.board[row + range][col] == opposite)
					blockFactor++;
				
				if(blockFactor <= block)
					count++;
				
			}
		}
		/*
		 * Finish Check Vertical.
		 */
		
		if((row + range <= length) && (col + range <= length)){
			rangeFactor = 1;
			int i, j;
			for(i = row + 1, j = col + 1; (i < row + range) && (j < col + range) && (rangeFactor != 0); i++, j++)
				if(this.board[i][j] != current)
					rangeFactor = 0;
			
			if(rangeFactor != 0){
				blockFactor = 0;
				
				if(row == 0 || col == 0)
					blockFactor++;
				
				else if((row >= 1) && (col >= 1) && this.board[row - 1][col - 1] == opposite)
					blockFactor++;
				
				else if((row + range == length) || (col + range == length))
					blockFactor++;
				
				else if((row + range < length) && (col + range < length) && 
						this.board[row + range][col + range] == opposite)
					blockFactor++;
				
				if(blockFactor <= block)
					count++;
			}
		}
		/*
		 * Finish one diagonal 
		 */
		
		/*
		 * Check second diagonal
		 */
		
		if((row + range <= length) && (col + 1 >= range)){
			rangeFactor = 1;
			int i, j;
			for(i = row + 1, j = col - 1; (i < row + range) && (j >= 0) && (rangeFactor != 0);
					i++, j--)
				if(this.board[i][j] != current)
					rangeFactor = 0;
			
			if(rangeFactor != 0){
				blockFactor = 0;
				
				if(row == length - 1 || col == range)
					blockFactor++;
				
				else if((row + 1 < length) && (col - 1 >= 0) && 
						this.board[row + 1][col - 1] == opposite)
					blockFactor++;
				
				else if(row == 0 || col == 14)
					blockFactor++;
				
				else  if((row - 1 >= 0) && (col + 1 < length) && 
						this.board[row - 1][col + 1] == opposite)
					blockFactor++;
				
				if(blockFactor <= block)
					count++;
			}
		}
		
		/*
		 * Finish second diagonal
		 */
		return count;
	}
}
