package project;

import java.util.Scanner;
import java.util.ArrayList;


public class FullAssembler implements Assembler {

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		
		if(error == null) {
			throw new IllegalArgumentException("Coding error: the error buffer is null");
		}
		
		ArrayList<String> fileIn = new ArrayList<String>();
		try(Scanner reader = new Scanner(inputFileName)){
			while(reader.hasNext()) {
				fileIn.add(reader.nextLine());
			}
		}
		
		int lineNum = 0;
		boolean blankLineFound = false;
		for(String line : fileIn) {
			if(line.trim().length() == 0 && blankLineFound == false) {
				blankLineFound = true;
				int errorLine = lineNum;
			} else if(line.trim().length() != 0 && blankLineFound == true) {
				error.append("\nIllegal blank line in the source file");
			}
			
			if(line.charAt(0) == ' ' || line.charAt(0) == '\t') {
				error.append("\nLine starts with illegal white space");
			}
			
			if(line.trim().toUpperCase().equals("DATA") && readingCode == true)
			
			lineNum++;
		}
		
		return 0;
	}
}
