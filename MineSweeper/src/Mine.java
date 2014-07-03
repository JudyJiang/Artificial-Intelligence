import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Mine is responsible for:
 * 1. information stored in a "back" end --- mine[][]
 * 2. information shown on board --- board[][]
 * 3. before taken each move, compare board and mine to see what further move
 *    needed
 */
public class Mine {
	private String[][] mine;
	private String[][] board;
	//private MineBlock[][] test;

	public String[][] getMine() {
		return mine;
	}

	public void setMine(String[][] mine) {
		this.mine = mine;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}

	public Mine(int sizei, int sizej) {
		this.board = new String[sizei][sizei];
		this.mine = new String[sizei][sizei];
	}

	public void initModel(int posx, int posy) {
		for (int i = 0; i < mine.length; i++) {
			for (int j = 0; j < mine[i].length; j++) {
				mine[i][j] = "0";
				board[i][j] = "+";
			}
		}
		/*
		 * Set bomb positions randomly (number less than the posy, which is the 
		 * setting bombs number
		 */
		for (int i = 0; i < posy; i++) {
			int x = new Random().nextInt(posx);
			int y = new Random().nextInt(posx);
			if (!mine[x][y].equals("@")) {
				mine[x][y] = "@";
				for (int k1 = 0; k1 < mine.length; k1++) {
					for (int k2 = 0; k2 < mine[k1].length; k2++) {
						if (mine[k1][k2].equals("@")) {
							continue;
						}
						/*
						 * Set blocks number arond a bomb accordingly
						 */
						if ((x - k1) * (x - k1) + (y - k2) * (y - k2) == 1
								|| (x - k1) * (x - k1) + (y - k2) * (y - k2) == 2) {
							mine[k1][k2] = Integer.toString(Integer
									.parseInt(mine[k1][k2]) + 1);
						}
					}
				}
			} else {
				i--;
			}
		}
	}

	/*
	 * Before control->mouse taking the click action
	 * Mine should compare what the board block compared to the mine block 
	 * will result in.
	 */
	public void Decide(int posx, int posy) {
		if (board[posx][posy].equals("*")) {
			return;
		}
		if (board[posx][posy].equals("%")) {
			return;
		}
		if (!mine[posx][posy].equals("0") && !mine[posx][posy].equals("@")) {
			if (!board[posx][posy].equals("*")) {
				board[posx][posy] = mine[posx][posy];
			}
		}
		if (mine[posx][posy].equals("@")) {
			for (int i = 0; i < mine.length; i++) {
				for (int j = 0; j < mine[posx].length; j++) {
					/*
					 * guess wrong, all the bombs shown up
					 */
					if (mine[i][j].equals("@") && !board[i][j].equals("*"))
						board[i][j] = mine[posx][posy];
					
					
					/*
					 * guess right.
					 */
					if (mine[i][j].equals("@") && board[i][j].equals("*"))
						board[i][j] = "$";
					/*
					 * guess wrong, wrong flag position
					 */
					if (!mine[i][j].equals("@") && board[i][j].equals("*"))
						board[i][j] = "#";
				}
			}
			board[posx][posy] = "&";
		}
	    /*
	     * To get blocks those're don't comtain bombs. how to show
	     * them by clicking the mouse (a wider range here..)
	     */
		else if (mine[posx][posy].equals("0")) {
			board[posx][posy] = mine[posx][posy];
			revealBlocks(posx,posy);
		}
	}
	
	/*
	 * Recursively find the current "null" (namely "0") block's adjacent 
	 * null block and show them out. 
	 */
	public void revealBlocks(int posx, int posy){
		for(int i = posx-1;i<=posx+1;i++){
			for(int j = posy-1;j<=posy+1;j++){
				if(i>=0 && i<mine.length && j>=0 && j<mine.length){
					if(!mine[i][j].equals("*") && board[i][j].equals("+")){
						board[i][j] = mine[i][j];
						if(mine[i][j].equals("0"))
							revealBlocks(i,j);
					}
				}
			}
		}
	}
	
	public void addf(int a, int b) {
		if (!board[a][b].equals("*")
				&& !(board[a][b].equals("+") || board[a][b].equals("++"))
				&& !board[a][b].equals("%")) {
			return;
		}
		if (board[a][b].equals("*")) {
			board[a][b] = "%";
			return;
		}
		if (board[a][b].equals("%")) {
			board[a][b] = "++";
			return;
		}
		board[a][b] = "*";
	}

	
	public boolean Win(int num) {
		for (int i = 0; i < mine.length; i++) {
			for (int j = 0; j < mine[i].length; j++) {
				if (mine[i][j].equals("@") && !board[i][j].equals("*")
						|| num != 0) {
					return false;
				}
			}
		}
		for (int i = 0; i < mine.length; i++) {
			for (int j = 0; j < mine[i].length; j++) {
				if (board[i][j].equals("+")) {
					if (mine[i][j].equals("@")) {
						board[i][j] = "";
					} else {
						board[i][j] = mine[i][j];
					}
				}
			}
		}
		return true;
	}

	public boolean Fail() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].equals("@")) {
					return true;
				}
			}
		}
		return false;
	}

	public String[][] getBoard() {
		return this.board;
	}

	public void addchess(int i, int j) {
		board[i][j] = "*";
	}

	public boolean see(int x, int y) {
		String str = board[x][y];
		if (!(str.equals("1") || str.equals("2") || str.equals("3")
				|| str.equals("4") || str.equals("5") || str.equals("6")
				|| str.equals("7") || str.equals("8"))) {
			return false;
		}
		int minenum = 0;
		for (int i = 0; i < mine.length; i++) {
			for (int j = 0; j < mine[i].length; j++) {
				if ((x - i) * (x - i) + (y - j) * (y - j) == 1
						|| (x - i) * (x - i) + (y - j) * (y - j) == 2) {
					if (board[i][j].equals("*")) {
						minenum++;
					}
				}
			}

		}
		if (Integer.parseInt(board[x][y]) == minenum) {
			return true;
		}
		return false;
	}
}
