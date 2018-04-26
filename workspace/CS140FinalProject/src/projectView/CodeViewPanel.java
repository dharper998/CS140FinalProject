package projectView;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTextField;

import project.MachineModel;
import project.Memory;

@SuppressWarnings("deprecation")
public class CodeViewPanel implements Observer {
	MachineModel model;
	JScrollPane scroller;
	JTextField[] codeHex = new JTextField[Memory.CODE_MAX / 2];
	JTextField[] codeDecimal = new JTextField[Memory.CODE_MAX / 2];
	int previousColor = -1;
	
	public CodeViewPanel(ViewMediator gui, MachineModel mdl) {
		model = mdl;
		gui.addObserver(this);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
