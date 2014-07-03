/*
 * Timer is responsible for the timer (end, start of the game)
 */

public class Timer extends Thread {
	private int count = 0;
	private boolean togo=true;
	private Controller controller;
  public Timer(Controller controller){
	  this.controller=controller;
  }
	public void run() {
		while (togo) {
			try {
				sleep(1000);
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			controller.getWin().getTime().setText("  "+Integer.toString(count));
		}
	}
	public int getcount() {
		return count;
	}
	public void setcount(int i) {
		this.count = i;
	}
	public boolean isGoon() {
		return togo;
	}
	public void setGoon(boolean goon) {
		this.togo = goon;
	}
	public Controller getController() {
		return controller;
	}
	public void setController(Controller controller) {
		this.controller = controller;
	}

	
}
