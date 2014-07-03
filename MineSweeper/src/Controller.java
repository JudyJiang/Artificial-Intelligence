import javax.swing.JOptionPane;
/*
 * Controller is responsible to connect each part together
 * * Check the block before mouse click;
 * * Listen to mouse movement
 * * Listen to window/option window change.
 */
public class Controller {
	private Window win;
	private Mine model;
	private int boardsize = 0;/* size of the mine, can change with option*/
	private int num = 0;     /* number of bombs, change with option*/
	private int Aa = 0;     /* remain bomb number, also shown in the window part*/
	private Timer timer;
	private MineBlock[][] board;  

	public Controller(Mine model, int boardsize, int num) {
		this.boardsize = boardsize;
		this.num = num;
		this.model = model;
		init();
	}

	private void init() {
		board = new MineBlock[boardsize][boardsize];
		win = new Window(this);
	}
	
	/*
	 * Controller starts the game
	 */
	public void start() {
		win.getNumber().setText("  " + Integer.toString(num));
		model.initModel(boardsize, num); 
		Aa = num;
		showgame();
		if (timer != null) 
			timer.setGoon(false);
		timer = new Timer(this);
		timer.start();
		win.toShow();
	}

	public void exit() {
		int val = JOptionPane.showConfirmDialog(win, "Sure to Leave?");
		if (val == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	/*
	 * board (type MineBlock) set the block image icon dynamically 
	 * according to the assignment and change of the model.getBoard(for shown)
	 * part.
	 */
	private void showgame() {
		String[][] c = model.getBoard();
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				String str = c[i][j];
				board[i][j].setMineBlock(str);
			}
		}
	}

	public void movein(int i, int j) {
		if (model.getBoard()[i][j].equals("+")) {
			model.getBoard()[i][j] = "++";
		}
		showgame();
	}

	public void moveout(int i, int j) {
		if (model.getBoard()[i][j].equals("++")) {
			model.getBoard()[i][j] = "+";
		}
		showgame();
	}

	/*
	 * Before click the position, controller needs 
	 * model (type Mine) to decide which move to take 
	 * next. (Mine make the decision by comparing the board(for shown)
	 * and mine(for storing)
	 * Also controller decides whether or not take this click will win or lose
	 */
	public void click(int i, int j) {
		if(timer!=null&&!timer.isGoon()){
			return;
		}
		model.Decide(i, j);
		showgame();
		if (model.Win(Aa)) {
			showgame();
			timer.setGoon(false);
			int val = JOptionPane.showConfirmDialog(win, "You Win");
			if (val == JOptionPane.YES_OPTION) {
				timer = new Timer(this);
				start();
			}
		}
		if (model.Fail()) {
			timer.setGoon(false);
			new Thread() {
				public void run() {
					int val = JOptionPane.showConfirmDialog(win,
							"You Lose, restart? ");
					if (val == JOptionPane.YES_OPTION) {
						if(timer!=null){
							timer.setGoon(false);
						}
						timer = new Timer(Controller.this);
						Controller.this.start();
					}
				};

			}.start();

		}
	}
	
	public void seesee(int x, int y) {
		if (model.see(x, y)) {
			for (int i = 0; i < model.getMine().length; i++) {
				for (int j = 0; j < model.getMine()[i].length; j++) {
					if ((x - i) * (x - i) + (y - j) * (y - j) == 1|| (x - i) * (x - i) + (y - j) * (y - j) == 2) {
						if (model.getBoard()[i][j].equals("+")|| model.getBoard()[i][j].equals("++")) {
							if (timer.isGoon()) 
								click(i, j);
							else 
								return;		
						}
					}
				}
			}
		}
	}

	/*
	 * Change the option window,take the change parameter 
	 * and set them to the game.
	 */
	public void changeOption(OptionWin owc) {

		if (owc.getText1().getText().matches("[0-9]+")
				&& owc.getText1().getText().matches("[0-9]+")) {

			int a = Integer.parseInt(owc.getText1().getText());
			int b = Integer.parseInt(owc.getText2().getText());
			owc.dispose();
			win.dispose();
			timer.setGoon(false);
			if (a > 20) {
				a = 20;
			}
			if (a < 3) {
				a = 3;
			}
			Mine minem = new Mine(a, b);
			new Controller(minem, a, b).start();
		} else {
			JOptionPane.showMessageDialog(owc, "Should be a number");
			owc.getText1().setText("");
			owc.getText2().setText("");
		}

	}

	/*
	 * Controller take the add flag (mouse command) and check the timer
	 * make the model (type Mine) to see whether to add flag
	 * take the results and set the make the result shown on the window part
	 */
	public void addFlag(int i, int j) {
		if(!timer.isGoon()){
			return;
		}
		model.addf(i, j);
		String[][] c = model.getBoard();
		if (c[i][j].equals("+") || c[i][j].equals("++")) {
			win.getNumber().setText("  " + Integer.toString(++Aa));
		} else if (c[i][j].equals("%")) {
			win.getNumber().setText("  " + Integer.toString(Aa));
		} else {
			if (board[i][j].getIcon() == MineBlock.block
					|| board[i][j].getIcon() == MineBlock.deadblock) {
				win.getNumber().setText("  " + Integer.toString(--Aa));
			}

		}
		showgame();
		/*
		 * Sometims adding a flag will cause the game to an end.
		 */
		if (model.Win(Aa)) {
			showgame();
			timer.setGoon(false);
			int val = JOptionPane.showConfirmDialog(win, "You Win, restart? ");
			if (val == JOptionPane.YES_OPTION) {
				timer.setGoon(false);
				timer = new Timer(this);
				start();
			}
		}
		
		if (model.Fail()) {
			timer.setGoon(false);
			int val = JOptionPane.showConfirmDialog(win, "You Lose, another one? ");
			if (val == JOptionPane.YES_OPTION) {
				timer.setGoon(false);
				timer = new Timer(this);
				start();
			}
		}
	}

	public Window getWin() {
		return win;
	}

	public void setWin(Window win) {
		this.win = win;
	}

	public Mine getModel() {
		return model;
	}

	public void setModel(Mine model) {
		this.model = model;
	}

	public int getBoardsize() {
		return boardsize;
	}

	public void setBoardsize(int boardsize) {
		this.boardsize = boardsize;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getAa() {
		return Aa;
	}

	public void setAa(int aa) {
		this.Aa = aa;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public MineBlock[][] getBoard() {
		return board;
	}

	public void setBoard(MineBlock[][] board) {
		this.board = board;
	}

}
