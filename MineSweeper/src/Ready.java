import javax.swing.JWindow;

public class Ready extends JWindow {
	private static final long serialVersionUID = 1L;
	private Controller con;
	private boolean prepared; /*Judge whether ready to start the game*/

	public Ready(Controller con) {
		this.con = con;
	}

	public void toShow() {
		setVisible(true);
		new Thread() {
			public void run() {
				try {
					sleep(1500);
					prepared = false;
					while (!prepared) {
						sleep(500);
						if (con != null) {
							prepared = true;
							dispose();
							sleep(500);
							con.start();

						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			};
		}.start();

		new Thread() {
			public void run() {
				try {
					prepared = false;
					while (!prepared)
						sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	public void setCon(Controller con){
		this.con = con;
	}
	
	public Controller getCon() {
		return con;
	}
}