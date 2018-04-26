package project;

import java.util.Scanner;
import java.util.ArrayList;


public class FullAssembler implements Assembler {
	private boolean readingCode = true;

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
		
		int errorLine;
		int lineNum = 0;
		boolean blankLineFound = false;
		for(String line : fileIn) {
			//ERROR 1
			if(line.trim().length() == 0 && blankLineFound == false) {
				blankLineFound = true;
				errorLine = lineNum;
			} else if(line.trim().length() != 0 && blankLineFound == true) {
				error.append("\nIllegal blank line in the source file");
				errorLine = lineNum;
			}
			
			//ERROR 2
			if(line.charAt(0) == ' ' || line.charAt(0) == '\t') {
				error.append("\nLine starts with illegal white space");
				errorLine = lineNum;
			}
			
			//ERROR 3
			if(line.trim().toUpperCase().equals("DATA") && readingCode == true) {
				if(!line.trim().equals("DATA")) {
					error.append("\nLine does not have DATA in upper case");
					errorLine = lineNum;
				}
			} else if(line.trim().equals("DATA") && readingCode == false) {
				error.append("\nSecond separator found");
			}
			
			
			
			lineNum++;
		}
		
		return errorLine;
	}
}
