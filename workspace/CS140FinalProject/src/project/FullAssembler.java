package project;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
		} catch(FileNotFoundException e) {
			error.append("\nUnable to open the source file");
			return -1;
		}
		
		int errorLine = 0;
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
			
			//ERROR 4
			if(readingCode == true) {
				String[] parts = line.trim().split("\\s+");
				if(!InstrMap.toCode.keySet().contains(parts[0])) {
					error.append("\nError on line " + (errorLine+1) + ": illegal mnemonic");
					errorLine = lineNum;
				} else {
					if(!parts[0].equals(parts[0].toUpperCase())) {
						error.append("\nError on line" + (errorLine+1) + ": illegal mnemonic");
					} else {
						//ERROR 5
						if(noArgument.contains(parts[0])) {
							if(parts[0].length() != 1) {
								error.append("\nError on line " + (errorLine+1) + ": this mnemonic cannot take arguments");
							}
						} else {
							if(parts[0].length() > 2) {
								error.append("\nError on line " + (errorLine+1) + ": this mnemonic has too many arguments");
							} else if(parts[0].length() < 2) {
								error.append("\nError on line " + (errorLine+1) + ": this mnemonic is missing an argument");
							}
							//ERROR 6
							try{
								int arg = Integer.parseInt(parts[1],16);
							} catch(NumberFormatException e) {
								error.append("\nError on line " + (errorLine+1) + ": argument is not a hex number");
								errorLine = lineNum;	
							}
						}
					}
				}
			}
			
			
			
			lineNum++;
		}
		
		try (PrintWriter output = new PrintWriter(outputFileName)){
			for(String s : outputCode) output.println(s);
		} catch (FileNotFoundException e) {
			error.append("\nError: Unable to write the assembled program to the output file");
			errorLine = -1;
		} catch (IOException e) {
			error.append("\nUnexplained IO Exception");
			errorLine = -1;
		}
		
		return errorLine;
	}
}
