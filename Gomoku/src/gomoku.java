/***
 * Fengjiao Jiang
 * fj2194
 */

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gomoku {
	public AI ai;
	public AI ai2;

	/*
	 * Player information
	 */

	public static enum Player {
		HUMAN, RANDOM, COMPUTER
	};

	public Player initialPlayer;
	public Player currentPlayer;
	public int mode;

	/*
	 * Each player's play piece type
	 */
	public Piece computerPiece;
	public Piece otherPiece;

	/*
	 * Time limit
	 */
	public static long start;
	// public static long end;
	public static long process;

	public static long period = 0;

	/*
	 * Board information
	 */
	public int board_size = 0;
	public int win_chain = 0;
	Board board;

	/*
	 * Current utilization.
	 */
	public int index;
	Scanner in;
	public boolean done = false;

	public gomoku() {
		in = new Scanner(System.in);
	}

	/*
	 * DONE: Mode selection and player initialization. Board initialization into
	 * all Empty status.
	 */
	public void initialize() {
		System.out.println("Initialize");
		board = new Board(board_size, win_chain);
		board.initialize();
		if (mode == 1) {
			initialPlayer = Player.HUMAN;
			currentPlayer = initialPlayer;
			System.out.println(currentPlayer);
			otherPiece = Piece.X;
			computerPiece = Piece.O;
			ai = new AI(computerPiece);
		}

		else if (mode == 2) {
			initialPlayer = Player.RANDOM;
			currentPlayer = initialPlayer;
			otherPiece = Piece.X;
			computerPiece = Piece.O;
			ai = new AI(computerPiece);
			
		} else if (mode == 3) {
			initialPlayer = Player.COMPUTER;
			currentPlayer = initialPlayer;

			computerPiece = Piece.X;
			otherPiece = Piece.O;

			ai2 = new AI(otherPiece);
			ai = new AI(computerPiece);

		} else {
			System.exit(0);
		}
	}

	/*
	 * TODO: Human Player Choice; System input in the type form of (x,y); Board
	 * addPiece.
	 */
	public void humanChoice() {
		System.out.println("Human Play, please input in the type of x" + " , "
				+ "y");
		String str = this.getInput();
		int pos_x = -1, pos_y = -1;
		int count = 1;
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(str);

		while (m.find()) {
			if (count == 1)
				pos_x = Integer.valueOf(m.group());
			else if (count == 2)
				pos_y = Integer.valueOf(m.group());
			if (count == 3) {
				System.out.println("Computer Win, you put wrong piece");
				System.exit(0);
			}
			count++;
		}

		if (board.addPiece(pos_x, pos_y, otherPiece))
			board.printBoard();
		else {
			System.out.println("Add invalid piece, computer Wins");
			System.exit(0);
		}
	}

	/*
	 * TODO: Random Player Choice; Randomly generated choice Board addPiece
	 */
	public void randomChoice() {
		System.out.print("Randome choice ");
		Random r = new Random();
		int pos_x = r.nextInt(this.board_size);
		int pos_y = r.nextInt(this.board_size);
		
		while(this.board.board[pos_x][pos_y] != Piece.EMPTY){
			pos_x = r.nextInt(this.board_size);
			pos_y = r.nextInt(this.board_size);
		}
		this.board.addPiece(pos_x, pos_y, otherPiece);
		System.out.println(this.otherPiece + " Movess " + pos_x + " " + pos_y);
		this.board.printBoard();
		System.out.println();

	}

	/*
	 * Computer Choice: Take the input from the AI class. getMove(); Finish the
	 * Move class.
	 */
	public void computerChoice() {
		System.out.print("Computer Play! " + "Type is " + this.ai.computerPiece);
		if (this.board.isEmpty()) {
			Position c_move = new Position(this.board_size / 3,
					this.board_size / 3);
			System.out.println(" Moves to " + c_move.pos_x + " " + c_move.pos_y);
			board.addPiece(c_move.pos_x, c_move.pos_y, computerPiece);
			board.printBoard();
			System.out.println();
			return;
		}

		Position c_move = new Position(-1, -1);
		c_move = ai.getMove(board);

		if (c_move != null && (c_move.pos_x != -1 || c_move.pos_y != -1)) {
			System.out.println(" Moves to " + c_move.pos_x + " " + c_move.pos_y);
			board.addPiece(c_move.pos_x, c_move.pos_y, ai.computerPiece);
			board.printBoard();
			System.out.println();
		}

		else if (c_move.pos_x == -1 || c_move.pos_y == -1) {
			System.out.println(computerPiece + " " + otherPiece);
			if (this.board.isWinnder(computerPiece))
				System.out.println("A Winner " + computerPiece);
			else if (this.board.isWinnder(otherPiece))
				System.out.println("A Winner " + otherPiece);
			else
				{System.out.println("Broken again");
				
			board.printBoard();
			System.exit(0);}
		}
	}

	/*
	 * Switch the player,
	 */
	public void switchPlayer() {
		if (currentPlayer == Player.COMPUTER) {
			if (mode == 1)
				currentPlayer = Player.HUMAN;
			else if (mode == 2)
				currentPlayer = Player.RANDOM;
			else if (mode == 3) {
				if (this.ai.computerPiece == Piece.X)
					ai = new AI(Piece.O);
				else
					ai = new AI(Piece.X);
			}
		} else if (currentPlayer == Player.RANDOM
				|| currentPlayer == Player.HUMAN)
			currentPlayer = Player.COMPUTER;
	}

	/*
	 * DONE:Ask if the player wants to play the game again.
	 */
	public boolean playAgain() {
		boolean again = true;
		System.out.println("Play again, y for yes, n to exit");
		String r = this.getInput();

		if (r.equals("y"))
			return true;
		else
			return false;
	}

	public String getInput() {
		String str = "";
		this.in = new Scanner(System.in);
		str = in.nextLine().toLowerCase();
		return str;
	}

	/*
	 * If the game is finished. Condition 1: The board is full; Condition 2:
	 * Either computer human is a winner.
	 */

	/*
	 * judge a winner in computer / human If either wins, print the board and
	 * show who's the winner.
	 */
	public boolean isFinished() {
		if (board.isFull()) {
			done = true;
		} else if (isComputerWin() || isOtherWin())
			done = true;

		else
			done = false;
		return done;
	}

	public boolean isComputerWin() {
		if (board.isWinnder(computerPiece)) {
			System.out.println("Computer Win");
			board.printBoard();
			return true;
		} else
			return false;
	}

	public boolean isOtherWin() {
		if (board.isWinnder(otherPiece)) {
			System.out.println("You win");
			board.printBoard();
			return true;
		} else
			return false;
	}

	/*
	 * Ask input of: board size; win size (5 or 3 or..); play mode,
	 * initialization of parameters;
	 */
	public void askInitialInput(Scanner in) {
		System.out.print("Board Size: \n");
		board_size = in.nextInt();
		System.out.print("Chain size, must be smaller than or equal to board size: \n");
		win_chain = in.nextInt();
		System.out.print("Identify play model, 1 for human, 2 for random, "
				+ "3 for computer\n");
		mode = in.nextInt();
		System.out.print("Time Period for the Game\n");
		period = in.nextLong() * 1000;

	}

	/*
	 * Main loop of the program, in charge of: 1. ask parameter input and board
	 * initialization; 2. play game when game is not in the "finished"
	 * condition; 3. switch player. 4. if game finishes, ask the user if want to
	 * play again.
	 */
	public void playGame() {
		askInitialInput(in);
		System.out
				.println(this.board_size + " " + win_chain + " " + mode
						+ "Game time is " + period / 1000 + "s");
		initialize();
		board.printBoard();

		
		if (mode == 1 || mode == 2) {
			start = System.currentTimeMillis();
			ai.setStartTime(start);
			ai.setPeriodTime(period);
			while (!isFinished() && !done) {
				if (currentPlayer == Player.HUMAN)
					humanChoice();
				else if (currentPlayer == Player.RANDOM)
					randomChoice();
				else if (currentPlayer == Player.COMPUTER){
					start = System.currentTimeMillis();
					ai.setStartTime(start);
					ai.setPeriodTime(period);
					computerChoice();
				}
				switchPlayer();
			}
		}
		if (mode == 3) {
			start = System.currentTimeMillis();
			ai.setStartTime(start);
			ai2.setStartTime(start);
			ai.setPeriodTime(period);
			ai2.setPeriodTime(period);
			while (!isFinished()) {
				start = System.currentTimeMillis();
				ai.setStartTime(start);
				ai2.setStartTime(start);
				ai.setPeriodTime(period);
				ai2.setPeriodTime(period);
				computerChoice();
				switchPlayer();
			}
			if(isFinished()){
				if(this.board.isWinnder(ai.computerPiece))
					System.out.println(ai.computerPiece + " Win");
				else
					System.out.println(ai.playerPiece + " Win");
				}
		}
		in.close();
	}

	public static void main(String[] args) {
		gomoku g = new gomoku();
		g.playGame();
	}
}