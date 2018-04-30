package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Loader {
	public static String load(MachineModel model, File file, int codeOffset, int memoryOffset) {
		int codeSize = 0;
		if(model == null || file == null) {
			return null;
		}
		Scanner input;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "File " + file.getName() + " Not Found";
		}
		boolean inCode = true;
		while(input.hasNextLine()) {
			String line1 = input.nextLine();
			String line2 = input.nextLine();
			Scanner parser = new Scanner(line1 + " " + line2);
			int num = parser.nextInt();
			if(inCode && num == -1) {
				inCode = false;
			} else if(inCode && num != -1) {
				int arg = parser.nextInt();
				model.setCode(codeOffset + codeSize, num, arg);
				codeSize++;
			} else {
				int arg = parser.nextInt();
				model.setData(num + memoryOffset, arg);
			}
			parser.close();
		}
		input.close();
		return "" + codeSize;
	}

	public static void main(String[] args) {
		MachineModel model = new MachineModel();
		String s = Loader.load(model, new File("factorial8.pexe"),100,200);
		for(int i = 100; i < 100 + Integer.parseInt(s); i++) {
			System.out.println("OP: " + model.getOp(i));			
			System.out.println("ARG: "+ model.getArg(i));			
		}
		for (int i = 200; i < 203; i++)
		System.out.println(i + " " + model.getData(i));
	}
}
