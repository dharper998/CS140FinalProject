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
			return null;
		}
		 boolean inCode = true;
		 while(input.hasNextLine()) {
			 String line1 = input.nextLine();
			 String line2 = input.nextLine();
			 Scanner parser = new Scanner(line1 + " " + line2);
			 int opcode = parser.nextInt();
			 if(inCode && opcode == -1) {
				 inCode = false;
			 } else if(inCode && opcode != -1) {
				 int arg = parser.nextInt();
				 Memory.setCode();
			 }
			 parser.close();
		 }
		 input.close();
		 return null;
	 }
}
