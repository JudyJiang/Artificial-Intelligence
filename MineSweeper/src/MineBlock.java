import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
/*
 * MineBlock is responsible for each block's property and methods
 * Corporate with Controller(one in charge of connecting the option window, 
 * main window, mouse movements and other objects together 
 */
public class MineBlock extends JLabel {

	private static final long serialVersionUID = 1L;
	private int posx;
	private int posy;
	private boolean right;
	private boolean left;
	public static final ImageIcon block = new ImageIcon(
			MineBlock.class.getResource("block.png"));
	public static final ImageIcon deadblock = new ImageIcon(
			MineBlock.class.getResource("deadblock.png"));
	
	public static final ImageIcon one = new ImageIcon(
			MineBlock.class.getResource("1.png"));
	public static final ImageIcon two = new ImageIcon(
			MineBlock.class.getResource("2.png"));
	public static final ImageIcon three = new ImageIcon(
			MineBlock.class.getResource("3.png"));
	public static final ImageIcon four = new ImageIcon(
			MineBlock.class.getResource("4.png"));
	public static final ImageIcon five = new ImageIcon(
			MineBlock.class.getResource("5.png"));
	public static final ImageIcon six = new ImageIcon(
			MineBlock.class.getResource("6.png"));
	public static final ImageIcon seven = new ImageIcon(
			MineBlock.class.getResource("7.png"));
	public static final ImageIcon eight = new ImageIcon(
			MineBlock.class.getResource("8.png"));
	
	public static final ImageIcon fl = new ImageIcon(
			MineBlock.class.getResource("flagl.png"));
	public static final ImageIcon flag = new ImageIcon(
			MineBlock.class.getResource("flag.png"));
	public static final ImageIcon guessright = new ImageIcon(
			MineBlock.class.getResource("flag.png"));
	public static final ImageIcon guesswrong = new ImageIcon(
			MineBlock.class.getResource("guesswrong.png"));
	public static final ImageIcon question = new ImageIcon(
			MineBlock.class.getResource("ques.png"));
	public static final ImageIcon onbomb = new ImageIcon(
			MineBlock.class.getResource("onbomb.png"));
	public static final ImageIcon bombs = new ImageIcon(
			MineBlock.class.getResource("bombs.png"));
	
    /*
     * Decide how to draw each block. Judge before press the mouse 
     * button. right and left are used to see if mouse is pressed and which
     * one is used
     */
	public MineBlock(final Controller con) {
		setBorder(new LineBorder(new Color(30, 10, 180)));
		setHorizontalAlignment(JLabel.CENTER);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((!right) && e.getButton() == 1) {
					con.click(posx, posy);
				}
				if ((!left) && e.getButton() == 3) {
					con.addFlag(posx, posy);
				}
				if (right && e.getButton() == 1) {/*in case*/
					con.seesee(posx, posy);
				}
				if (left && e.getButton() == 3) {
					con.seesee(posx, posy);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1) {
					left = true;
				}
				if (e.getButton() == 3) {
					right = true;
				}

			}

		
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1) {
					left = false;
				}
				if (e.getButton() == 3) {
					right = false;
				}
			}

			@Override
			/*
			 *Change the block type when mouse moving
			 */
			public void mouseEntered(MouseEvent e) {
				con.movein(posx, posy);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				con.moveout(posx, posy);
			}
			
		});

	}

	/*
	 * Take string each time and decide which block icon to set
	 */
	public void setMineBlock(String str) {
		String s = str;
		setIcon(block);
		if (!s.equals("@") && !s.equals("+")&&!s.equals("++") && !s.equals("*")
				&& !s.equals("$") && !s.equals("#") && !s.equals("&")
				&& !s.equals("%")) {
			switch (Integer.parseInt(s)) {
			case 0:
				setIcon(null);
				break;
			case 1:
				setIcon(one);
				break;
			case 2:
				setIcon(two);
				break;
			case 3:
				setIcon(three);
				break;
			case 4:
				setIcon(four);
				break;
			case 5:
				setIcon(five);
				break;
			case 6:
				setIcon(six);
				break;
			case 7:
				setIcon(seven);
				break;
			case 8:
				setIcon(eight);
				break;
			default:
				break;
			}
		}
		if(s.equals("++")){
			setIcon(deadblock);
		}
		if (s.equals("$")) {/*a right guess (shown when game over)*/
			setIcon(guessright);
		}
		if (s.equals("#")) {/* a wrong guess (shown when game over)*/
			setIcon(guesswrong);
		}
		if (s.equals("*")) {/*shown when set a flag on block*/
			setIcon(flag);
		}
		if (s.equals("@")) {/* game over, show bomb fields*/
			setIcon(bombs);
		}
		if (s.equals("&")) {/* the bomb cause trouble*/
			setIcon(onbomb);
		}
		if (s.equals("%")) {/* a question sign*/
			setIcon(question);
		}
	}

	public MineBlock(int posx, int posy, Controller con) {
		this(con);
		this.posx = posx;
		this.posy = posy;
	}
}
