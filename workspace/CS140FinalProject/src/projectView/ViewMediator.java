package projectView;

import java.awt.Component;

import project.MachineModel;

public class ViewMediator /*extends Observable*/ {
	private MachineModel model;
	
	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	public void step() {}

	public Component getFrame() {
		// TODO Auto-generated method stub
		return null;
	}
}
