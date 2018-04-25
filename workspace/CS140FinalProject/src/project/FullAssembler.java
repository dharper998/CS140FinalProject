package project;

import java.util.Scanner;
import java.util.ArrayList;


public class FullAssembler implements Assembler {

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		ArrayList<String> fileIn = new ArrayList<String>();
		try(Scanner reader = new Scanner(inputFileName)){
			while(reader.hasNext()) {
				fileIn.add(reader.nextLine());
			}
		}
		return 0;
	}
}
