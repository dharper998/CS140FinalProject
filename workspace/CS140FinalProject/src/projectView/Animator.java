package projectView;

import javax.swing.Timer;

public class Animator {
	private static final int TICK = 500;
	private boolean autoStepOn = false;
	Timer timer;
	ViewMediator view;
	
	public Animator(ViewMediator v) {
		view = v;
	}

	public boolean isAutoStepOn() {
		return autoStepOn;
	}

	public void setAutoStepOn(boolean autoStepOn) {
		this.autoStepOn = autoStepOn;
	}
	
	void toggleAutoStep() {
		autoStepOn = !autoStepOn;
	}
	
	void setPeriod(int period) {
		timer.setDelay(period);
	}
	
	public void start() {
		timer = new Timer(TICK, e -> {if(autoStepOn) view.step();});
		timer.start();
	}
}
