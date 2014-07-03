import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/*
 * Window is responsible for the create 
 * Mine frame, the toolkit, the option, start, timer postion on
 * the board.
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea time;
	private JTextArea number;
	private Mine mine;
	private Controller controller;

	public Window(Controller controller) {
		this.controller = controller;
		init();
	}

	public void init() {
		setTitle("MineSweeper");
		setSize(600,600);
		setResizable(false);
		setContentPane(createBoard());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.exit();
			}
		});
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

	}

	public void toShow() {
		setVisible(true);
	}

	private Container createBoard() {
		JPanel main = new JPanel(new BorderLayout());
		main.add(BorderLayout.CENTER, createrMine());
		main.add(BorderLayout.NORTH, createTool());
		return main;
	}

	private Component createTool(){
		JPanel main = new JPanel();
		JPanel main1 = new JPanel();
		JPanel main2 = new JPanel();
		Color back = Color.gray;
		Color fore = Color.black;
		Font font = new Font(Font.MONOSPACED,Font.TYPE1_FONT,15);
		JLabel Timer = new JLabel("Time:");
		JLabel Bombs = new JLabel("Bombs:");
		time = new JTextArea();
		number = new JTextArea();
		time.setText("0");
		
		Timer.setBackground(back);
		Timer.setForeground(fore);
		Timer.setFont(font);
		
		Bombs.setBackground(back);
		Bombs.setForeground(fore);
		Bombs.setFont(font);
		
		time.setBackground(back);
		time.setForeground(fore);
		time.setFont(font);
		
		number.setBackground(back);
		number.setForeground(fore);
		number.setFont(font);
		
		JMenuBar Bar = new JMenuBar();
		JMenu settings = new JMenu("Settings");
		settings.setBackground(back);
		settings.setForeground(fore);
		settings.setFont(font);
		setJMenuBar(Bar);
		JMenuItem start = new JMenuItem("New Game");
		JMenuItem options = new JMenuItem("Options");
		JMenuItem exit = new JMenuItem("Exit");
		
		JButton bstart = new JButton("Start");
		bstart.setBackground(back);
		bstart.setForeground(fore);
		bstart.setFont(font);
		
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.start();
			}
		});
		
		bstart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					controller.start();
				}
		});
		
		options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				OptionWin c = new OptionWin(controller);
				c.toShow();
			}
		});
		
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.exit();
			}
		});
		
		
        main1.add(Timer);
		main1.add(time);
		main1.add(bstart);
		main.add(main1);
		
		main2.add(Bombs);
		main2.add(number);
		main1.add(main2);
		main.add(main1);
		
		settings.add(start);
		settings.add(options);
		settings.add(exit);
		Bar.add(settings);
		
		
		return main;
	}

	private Component createrMine() {
		JPanel bor = new JPanel(new GridLayout(controller.getBoard().length+1,
				controller.getBoard()[0].length+1));
		
		bor.setBackground(Color.LIGHT_GRAY);
		int numberboardsize = controller.getBoard().length;
		int area = 16*numberboardsize;
        setSize(2*area,2*area);
        setResizable(true);
       
		for (int a = 0; a < controller.getBoard().length; a++) {
			for (int b = 0; b < controller.getBoard().length; b++) {
				controller.getBoard()[a][b] = new MineBlock(a, b, controller);
				controller.getBoard()[a][b].setIcon(MineBlock.block);
				//controller.getBoard()[a][b].setBorder(BorderFactory.createLineBorder(Color.gray, 2));
				bor.add(controller.getBoard()[a][b]);
			}
		}
		return bor;
	}

	public Mine getMine() {
		return mine;
	}

	public void setMine(Mine mine) {
		this.mine = mine;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public JTextArea getTime() {
		return time;
	}

	public void setTime(JTextArea time) {
		this.time = time;
	}

	public JTextArea getNumber() {
		return number;
	}

	public void setNumber(JTextArea number) {
		this.number = number;
	}

}
