package projectView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import project.MachineModel;
import project.Loader;

public class MemoryViewPanel implements Observer{
	private MachineModel model;
	private JScrollPane scroller;
	private JTextField[] dataHex;
	private JTextField[] dataDecimal;
	private int lower = -1;
	private int upper = -1;
	private int previousColor = -1;
	
	MemoryViewPanel(ViewMediator gui, MachineModel mdl, int lwr, int upr) {
		model = mdl;
		lower = lwr;
		upper = upr;
		gui.addObserver(this);
	}
	
	public JComponent createMemoryDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), 
				"Data Memory View [" + lower + "-" + upper + "]",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		panel.setBorder(border);
		JPanel innerPanel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(0, 1));
		JPanel decimalPanel = new JPanel();
		decimalPanel.setLayout(new GridLayout(0, 1));
		JPanel hexPanel = new JPanel();
		hexPanel.setLayout(new GridLayout(0, 1));
		innerPanel.add(numPanel, BorderLayout.LINE_START);
		innerPanel.add(decimalPanel, BorderLayout.CENTER);
		innerPanel.add(hexPanel, BorderLayout.LINE_END);
		
		dataHex = new JTextField[upper-lower];
		dataDecimal = new JTextField[upper-lower];
		
		for(int i=lower;i<upper;i++) {
			numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
			dataDecimal[i - lower] = new JTextField(10);
			dataHex[i-lower] = new JTextField(10);
			decimalPanel.add(dataDecimal[i-lower]);
			hexPanel.add(dataHex[i-lower]);
		}
		
		scroller = new JScrollPane(innerPanel);
		panel.add(scroller);
		return panel;
	}
}
