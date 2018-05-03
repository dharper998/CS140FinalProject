package project;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class FullAssembler implements Assembler {
	private boolean readingCode = true;

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {

		if(error == null) {
			throw new IllegalArgumentException("Coding error: the error buffer is null");
		}
		
		ArrayList<String> fileIn = new ArrayList<String>();
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		
		try(Scanner reader = new Scanner(inputFile)){
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
		int blankLineError = 0;
		for(String line : fileIn) {
			System.out.println(line);
			//ERROR 1
			if(line.trim().length() == 0 && blankLineFound == false) {
				blankLineFound = true;
				blankLineError = lineNum + 1;
				lineNum++;
				continue;
			} else if(line.trim().length() != 0 && blankLineFound == true && blankLineError != 0) {
				error.append("\nIllegal blank line in the source file at line: " + (blankLineError));
				errorLine = blankLineError;
				blankLineError = 0;
			} else if(line.trim().length() == 0 && blankLineFound == true) {
				lineNum++;
				continue;
			}
			
			//ERROR 2
			if(line.charAt(0) == ' ' || line.charAt(0) == '\t') {
				error.append("\nLine " + (lineNum+1) + " starts with illegal white space");
				errorLine = lineNum + 1;
			}
			
			//ERROR 3
			if(line.trim().toUpperCase().equals("DATA") && readingCode == true) {
				if(!(line.trim().equals("DATA"))) {
					error.append("\nLine " + (lineNum+1) + " does not have DATA in upper case");
					errorLine = lineNum + 1;
				} else {
					readingCode = false;
					lineNum++;
					continue;
				}
			} else if(line.trim().equals("DATA") && readingCode == false) {
				error.append("\nSecond separator found");
				errorLine = lineNum + 1;
			}
			
			//ERROR 4
			if(readingCode == true) {
				String[] parts = line.trim().split("\\s+");
				if(!(InstrMap.toCode.keySet().contains(parts[0]))) {
					error.append("\nError on line " + (lineNum+1) + ": illegal mnemonic");
					errorLine = lineNum + 1;
				} else {
					if(!(parts[0].equals(parts[0].toUpperCase()))) {
						error.append("\nError on line" + (lineNum+1) + ": illegal mnemonic");
						errorLine = lineNum + 1;
					} else {
						//ERROR 5
						if(noArgument.contains(parts[0])) {
							if(parts.length != 1) {
								error.append("\nError on line " + (lineNum+1) + ": this mnemonic cannot take arguments");
								errorLine = lineNum + 1;
							}
						} else {
							if(parts.length > 2) {
								error.append("\nError on line " + (lineNum+1) + ": this mnemonic has too many arguments");
								errorLine = lineNum + 1;
							} else if(parts.length < 2) {
								error.append("\nError on line " + (lineNum+1) + ": this mnemonic is missing an argument");
								errorLine = lineNum + 1;
							} else {
								//ERROR 6
								try{
									@SuppressWarnings("unused")
									int arg = Integer.parseInt(parts[1],16);
								} catch(NumberFormatException e) {
									error.append("\nError on line " + (lineNum+1) + ": argument is not a hex number");
									errorLine = lineNum + 1;	
								}
							}
						}
					}
				}
			} else {
				String[] parts = line.trim().split("\\s+");
				if(parts.length == 2) {
					try {
						@SuppressWarnings("unused")
						int address = Integer.parseInt(parts[0], 16);
					} catch(NumberFormatException e) {
						error.append("\nError on line " + (lineNum+1) + ": data has non-numeric memory address");
						errorLine = lineNum + 1;
					}
					try {
						@SuppressWarnings("unused")
						int value = Integer.parseInt(parts[1],16);
					} catch(NumberFormatException e) {
						error.append("\nError on line " + (lineNum+1) + ": data has non-numeric memory value");
						errorLine = lineNum + 1;					
					}
				} else if(parts.length > 2){
					error.append("\nError on line " + (lineNum+1) + ": data has too many arguments");
					errorLine = lineNum + 1;
				} else {
					error.append("\nError on line " + (lineNum+1) + ": data has too many arguments");
					errorLine = lineNum + 1;
				}
			}
			lineNum++;
		}
		
		if(errorLine == 0) {
			int i = new SimpleAssembler().assemble(inputFileName, outputFileName, error);
			System.out.println("result = " + i);
		}
		
		/*
		 * } catch (FileNotFoundException e) {
				error.append("\nError: Unable to write the assembled program to the output file");
				retVal = -1;
			} catch (IOException e) {
				error.append("\nUnexplained IO Exception");
				retVal = -1;
			}
			
			Leslie wants us to add this when outputting to the file but that isnt done in full assembler so ???
		 */
		
		return errorLine;
	}
}
