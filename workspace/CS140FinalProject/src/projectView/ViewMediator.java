package projectView;

import project.MachineModel;
import project.Memory;
import project.CodeAccessException;
import project.DivideByZeroException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("deprecation")
public class ViewMediator extends Observable {
	private MachineModel model;
	private CodeViewPanel codeViewPanel;
	private MemoryViewPanel memoryViewPanel1;
	private MemoryViewPanel memoryViewPanel2;
	private MemoryViewPanel memoryViewPanel3;
	private ControlPanel controlPanel;
	private ProcessorViewPanel processorPanel;
	private MenuBarBuilder menuBuilder;
	private JFrame frame;
	private FilesManager filesManager;
	private Animator animator;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ViewMediator mediator = new ViewMediator();
				MachineModel model = new MachineModel(
						true,  
						() -> mediator.setCurrentState(States.PROGRAM_HALTED)
						);
				mediator.setModel(model);
				mediator.createAndShowGUI();
			}
		});
	}

	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	public void step() {
		if(model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED) {
			try {
				model.step();
			} catch (CodeAccessException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}
			setChanged();
			notifyObservers();
		}
	}
	
	public void execute() {
		while(model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED) {
			try {
				model.step();
			} catch (CodeAccessException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getInstructionPointer() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}
		}
		setChanged();
		notifyObservers();
	}
	
	public void exit() {
		int decision = JOptionPane.showConfirmDialog(frame, "Do you really wish to exit?", "Confirmation", JOptionPane.YES_NO_OPTION);
		if (decision == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	private void createAndShowGUI() {
		animator = new Animator(this);
		filesManager = new FilesManager(this);
		filesManager.initialize();
		codeViewPanel = new CodeViewPanel(this, model);
		memoryViewPanel1 = new MemoryViewPanel(this, model, 0, 240);
		memoryViewPanel2 = new MemoryViewPanel(this, model, 240, Memory.DATA_SIZE/2);
		memoryViewPanel3 = new MemoryViewPanel(this, model, Memory.DATA_SIZE/2, Memory.DATA_SIZE);
		controlPanel = new ControlPanel(this);
		processorPanel = new ProcessorViewPanel(this, model);
		menuBuilder = new MenuBarBuilder(this);
		frame = new JFrame("Simulator");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout(1,1));
		content.setBackground(Color.BLACK);
		frame.setSize(1200, 600);
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1, 3));
		frame.add(codeViewPanel.createCodeDisplay(), BorderLayout.LINE_START);
		center.add(memoryViewPanel1.createMemoryDisplay());
		center.add(memoryViewPanel2.createMemoryDisplay());
		center.add(memoryViewPanel3.createMemoryDisplay());
		frame.add(center, BorderLayout.CENTER);
		frame.add(controlPanel.createControlDisplay(),BorderLayout.PAGE_END);
		frame.add(processorPanel.createProcessorDisplay(),BorderLayout.PAGE_START);
		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		bar.add(menuBuilder.createFileMenu());
		bar.add(menuBuilder.createExecuteMenu());
		bar.add(menuBuilder.createJobsMenu());		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(WindowListenerFactory.
				windowClosingFactory(e -> exit()));
		model.setCurrentState(States.NOTHING_LOADED);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
		animator.start();
		frame.setVisible(true);
	}

	public States getCurrentState() {
		return model.getCurrentState();
	}

	public void setCurrentState(States currentStates) {
		model.setCurrentState(currentStates);
		if(currentStates == States.PROGRAM_HALTED) {
			animator.setAutoStepOn(false);
		}
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
	}

	public void clearJob() {
		model.clearJob();
		model.setCurrentState(States.NOTHING_LOADED);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers("Clear");
	}

	public void makeReady(String s) {
		animator.setAutoStepOn(false);
		model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers(s);
	}

	public void toggleAutoStep() {
		animator.toggleAutoStep();
		if(animator.isAutoStepOn()) {
			model.setCurrentState(States.AUTO_STEPPING);
		} else {
			model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}
	}

	public void reload() {
		animator.setAutoStepOn(false);
		clearJob();
		filesManager.finalLoad_ReloadStep(model.getCurrentJob());
	}

	public void assembleFile() {
		filesManager.assembleFile();
	}

	public void loadFile() {
		filesManager.loadFile(model.getCurrentJob());
	}

	public void setPeriod(int value) {
		animator.setPeriod(value);
	}

	public void setJob(int i) {
		model.setJob(i);
		if(model.getCurrentState() != null) {
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}
	}
}
