package projectView;

import project.MachineModel;
import java.util.Observable;
import javax.swing.JFrame;

@SuppressWarnings("deprecation")
public class ViewMediator extends Observable {
	private MachineModel model;
	private JFrame frame;
	
	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	public void step() {}

	public JFrame getFrame() {
		return frame;
	}
}
