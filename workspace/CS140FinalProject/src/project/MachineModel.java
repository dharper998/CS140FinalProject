package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class MachineModel {
	private class CPU {
		private int accumulator;
		private int instructionPointer;
		private int memoryBase;
		
		
		public void incrementIP(int val) {
			instructionPointer += val;
		}
	}
	
	public final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap<Integer, Instruction>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;
	private boolean withGUI;
	private Job[] jobs = new Job[2];
	private Job currentJob;
	
	public MachineModel() {
		this(false, null);
	}

	public MachineModel(boolean b, HaltCallback hcb) {
		withGUI = b;
		callback = hcb;
		
		//INSTRUCTION_MAP entry for "NOP"
		INSTRUCTIONS.put(0x0, arg -> {
			cpu.incrementIP(1);
		});
		
		//INSTRUCTION_MAP entry for "LODI"
		INSTRUCTIONS.put(0x1, arg -> {
			cpu.accumulator = arg;
			cpu.incrementIP(1);
		});
		
		//INSTRUCTION_MAP entry for "LOD"
		INSTRUCTIONS.put(0x2, arg -> {
			int arg1 = memory.getData(cpu.memoryBase + arg);
			cpu.accumulator = arg1;
			cpu.incrementIP(1);
		});
		
		//INSTRUCTION_MAP entry for "LODN"
		INSTRUCTIONS.put(0x3, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator = arg2;
            cpu.incrementIP(1);
		});
		
		//INSTRUCTION_MAP entry for "STO"
		INSTRUCTIONS.put(0x4, arg -> {
			memory.setData(cpu.memoryBase + arg, cpu.accumulator);
			cpu.incrementIP(1);
		});
		
		//INSTRUCTION_MAP entry for "STON"
		INSTRUCTIONS.put(0x5, arg -> {
			int val = memory.getData(cpu.memoryBase+arg);
			memory.setData(cpu.memoryBase + val, cpu.accumulator);
			cpu.incrementIP(1);
		});
		
		//INSTRUCTION_MAP entry for "JMPR"
		INSTRUCTIONS.put(0x6, arg -> {
			cpu.instructionPointer += arg;
		});
		
		//INSTRUCTION_MAP entry for "JUMP"
		INSTRUCTIONS.put(0x7, arg -> {
			int val = memory.getData(cpu.memoryBase + arg);
			cpu.instructionPointer += val;
		});
		
		//INSTRUCTION_MAP entry for "JUMPI"
		INSTRUCTIONS.put(0x8, arg -> {
			cpu.instructionPointer = currentJob.getStartcodeIndex() + arg;
		});
		
		//INSTRUCTION_MAP entry for "JMPZR"
		INSTRUCTIONS.put(0x9, arg -> {
			if(cpu.accumulator == 0) {
				cpu.instructionPointer += arg;
			} else {
				cpu.incrementIP(1);
			}
		});
		
		//INSTRUCTION_MAP entry for "JMPZ"
		INSTRUCTIONS.put(0xA, arg -> {
			if(cpu.accumulator == 0) {
				int val = memory.getData(cpu.memoryBase + arg);
				cpu.instructionPointer += val;
			} else {
				cpu.incrementIP(1);
			}
		});
		
		//INSTRUCTION_MAP entry for "JMPZI"
		INSTRUCTIONS.put(0xB, arg -> {
			if(cpu.accumulator == 0) {
				cpu.instructionPointer = currentJob.getStartcodeIndex() + arg;
			} else {
				cpu.incrementIP(1);
			}
		});
		
		//INSTRUCTION_MAP entry for "ADDI"
        INSTRUCTIONS.put(0xC, arg -> {
            cpu.accumulator += arg;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADD"
        INSTRUCTIONS.put(0xD, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            cpu.accumulator += arg1;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADDN"
        INSTRUCTIONS.put(0xE, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator += arg2;
            cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "SUBI"
        INSTRUCTIONS.put(0xF, arg -> {
            cpu.accumulator -= arg;
            cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "SUB"
        INSTRUCTIONS.put(0x10, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            cpu.accumulator -= arg1;
            cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "SUBN"
        INSTRUCTIONS.put(0x11, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator -= arg2;
            cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "MULI"
        INSTRUCTIONS.put(0x12, arg -> {
            cpu.accumulator *= arg;
            cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "MUL"
        INSTRUCTIONS.put(0x13, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            cpu.accumulator *= arg1;
            cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "MULN"
        INSTRUCTIONS.put(0x14, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator *= arg2;
            cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "DIVI"
        INSTRUCTIONS.put(0x15, arg -> {
        	if(arg != 0) {
	            cpu.accumulator /= arg;
	            cpu.incrementIP(1);
        	} else {
        		throw new DivideByZeroException("Cannot divide by zero");
        	}
        });
        
        //INSTRUCTION_MAP entry for "DIV"
        INSTRUCTIONS.put(0x16, arg -> {
	        int arg1 = memory.getData(cpu.memoryBase+arg);
	        if(arg1 != 0) {
	            cpu.accumulator /= arg1;
	            cpu.incrementIP(1);
        	} else {
        		throw new DivideByZeroException("Cannot divide by zero");
        	}
        });
        
        //INSTRUCTION_MAP entry for "DIVN"
        INSTRUCTIONS.put(0x17, arg -> {
	        int arg1 = memory.getData(cpu.memoryBase+arg);
	        int arg2 = memory.getData(cpu.memoryBase+arg1);
	        if(arg2 != 0) {
	            cpu.accumulator /= arg2;
	            cpu.incrementIP(1);
        	} else {
        		throw new DivideByZeroException("Cannot divide by zero");
        	}
        });
        
        //INSTRUCTION_MAP entry for "ANDI"
        INSTRUCTIONS.put(0x18, arg -> {
        	if(cpu.accumulator != 0 && arg != 0) {
        		cpu.accumulator = 1;
        	} else {
        		cpu.accumulator = 0;
        	}
    		cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "AND"
        INSTRUCTIONS.put(0x19, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
        	if(cpu.accumulator != 0 && arg1 != 0) {
        		cpu.accumulator = 1;
        	} else {
        		cpu.accumulator = 0;
        	}
    		cpu.incrementIP(1);
        });
        
        //INSTSRUCTION_MAP entry for "NOT"
        INSTRUCTIONS.put(0x1A, arg -> {
        	if(cpu.accumulator != 0) {
        		cpu.accumulator = 0;
        	} else {
        		cpu.accumulator = 1;
        	}
    		cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "CMPL"
        INSTRUCTIONS.put(0x1B, arg -> {
        	int val = memory.getData(cpu.memoryBase + arg);
        	if(val < 0) {
        		cpu.accumulator = 1;
        	} else {
        		cpu.accumulator = 0;
        	}
        	cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "CMPZ"
        INSTRUCTIONS.put(0x1C, arg -> {
        	int val = memory.getData(cpu.memoryBase + arg);
        	if(val == 0) {
        		cpu.accumulator = 1;
        	} else {
        		cpu.accumulator = 0;
        	}
        	cpu.incrementIP(1);
        });
        
        //INSTRUCTION_MAP entry for "HALT"
        INSTRUCTIONS.put(0x1F, arg -> {
        	callback.halt();
        });
        
        
        
	}
	
	int[] getData() {
		return memory.getData();
	}

	public int getData(int index) {
		return memory.getData(index);
	}

	public void setData(int index, int value) {
		memory.setData(index, value);
	}
	
	public int getAccumulator() {
		return this.cpu.accumulator;
	}
	
	public void setAccumulator(int accumulator) {
		this.cpu.accumulator = accumulator;
	}
	
	public int getInstructionPointer() {
		return this.cpu.instructionPointer;
	}
	
	public void setInstructionPointer(int instructionPointer) {
		this.cpu.instructionPointer = instructionPointer;
	}
	
	public int getMemoryBase() {
		return this.cpu.memoryBase;
	}
	
	public void setMemoryBase(int memoryBase) {
		this.cpu.memoryBase = memoryBase;
	}
	
	public Instruction get(int index) {
		return INSTRUCTIONS.get(index);
	}
	
	int[] getCode() {
		return memory.getCode();
	}

	public int getOp(int i) {
		return memory.getOp(i);
	}

	public int getArg(int i) {
		return memory.getArg(i);
	}

	public void setCode(int index, int op, int arg) {
		memory.setCode(index, op, arg);
	}

	String getHex(int i) {
		return memory.getHex(i);
	}
	
	String getDecimal(int i) {
		return memory.getDecimal(i);
	}
	
	public Job getCurrentJob() {
		return currentJob;
	}
}
