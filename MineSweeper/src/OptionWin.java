/*
 * Option Window is (buttons, choices on the setting menu part
 * Connections between controller's functions.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class OptionWin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JEditorPane text1;
	private JEditorPane text2;
	private Controller controller;
	private Color color = Color.LIGHT_GRAY;
	private JRadioButton button1;
	private JRadioButton button2;
	private JRadioButton button3;

	public OptionWin(Controller controller) {
		this.controller = controller;
		init();
	}

	private void init() {
		setTitle("Level");
		setSize(300, 370);
		setContentPane(createpanel());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				OptionWin.this.setVisible(false);
			}
		});
	}

	public void toShow() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
		setVisible(true);

	}

	private JPanel createpanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.add(BorderLayout.NORTH, createNorth());
		main.add(BorderLayout.CENTER, createCenter());
		main.add(BorderLayout.SOUTH, createSouth());
		return main;
	}

	private Container createNorth() {
		Color color = Color.gray;
		JPanel main = new JPanel(new BorderLayout(0, 10));
		main.setBorder(new EmptyBorder(20, 20, 20, 20));

		JPanel center = new JPanel(new BorderLayout());
		JLabel label1 = new JLabel("Mine Size");
		JLabel label2 = new JLabel("Bomb Number");
		center.add(BorderLayout.NORTH, label1);
		center.add(BorderLayout.SOUTH, label2);
		JPanel east = new JPanel(new BorderLayout(0, 10));
		text1 = new JEditorPane();
		text1.setPreferredSize(new Dimension(150, 25));
		text2 = new JEditorPane();
		text2.setPreferredSize(new Dimension(150, 25));
		text1.setBackground(Color.LIGHT_GRAY);
		text2.setBackground(Color.LIGHT_GRAY);
		text1.setText("9");
		text2.setText("10");
		east.add(BorderLayout.NORTH, text1);
		east.add(BorderLayout.SOUTH, text2);
		main.add(BorderLayout.EAST, east);
		main.add(BorderLayout.CENTER, center);
		main.setBackground(color);
		east.setBackground(color);
		center.setBackground(color);

		return main;
	}

	private Container createSouth() {
		JPanel main = new JPanel();
		main.setBackground(color);
		JPanel south = new JPanel(new FlowLayout());
		JButton ok = new JButton("Yes");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.changeOption(OptionWin.this);
			}
		});
		JButton cancel = new JButton("No");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}

		});
		ok.setBackground(Color.black);
		cancel.setBackground(Color.black);
		south.add(ok);
		south.add(cancel);
		south.setBorder(new EmptyBorder(15, 0, 0, 0));
		south.setBackground(color);
		main.add(south);
		return main;
	}

	private Container createCenter() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(new EmptyBorder(10, 20, 20, 20));
		main.setBackground(color);
		JPanel lvl = new JPanel();
		lvl.setBackground(color);
		lvl.setBorder(new TitledBorder("Game Level"));
		JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
		panel.setBackground(color);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		ButtonGroup group = new ButtonGroup();
		button1 = new JRadioButton("Start", true);
		button2 = new JRadioButton("Middle");
		button3 = new JRadioButton("High");
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				text1.setText("9");
				text2.setText("10");

			}
		});
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				text1.setText("15");
				text2.setText("30");

			}
		});
		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				text1.setText("18");
				text2.setText("60");

			}
		});
		button1.setBackground(color);
		button2.setBackground(color);
		button3.setBackground(color);
		group.add(button1);
		group.add(button2);
		group.add(button3);
		panel.add(button1);
		panel.add(button2);
		panel.add(button3);
		lvl.add(panel);
		main.add(lvl);
		return main;
	}


	public JEditorPane getText1() {
		return text1;
	}

	public void setText1(JEditorPane text1) {
		this.text1 = text1;
	}

	public JEditorPane getText2() {
		return text2;
	}

	public void setText2(JEditorPane text2) {
		this.text2 = text2;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}
