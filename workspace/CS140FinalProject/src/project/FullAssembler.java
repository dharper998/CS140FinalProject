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
		} /*catch(FileNotFoundException e) {
			error.append("\nUnable to open the source file");
			return -1;
		}*/
		
		int errorLine = 0;
		int lineNum = 0;
		boolean blankLineFound = false;
		for(String line : fileIn) {
			//ERROR 1
			if(line.trim().length() == 0 && blankLineFound == false) {
				blankLineFound = true;
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
				} else {
					readingCode = false;
					continue;
				}
			} else if(line.trim().equals("DATA") && readingCode == false) {
				error.append("\nSecond separator found");
			}
			
			//ERROR 4
			if(readingCode == true) {
				String[] parts = line.trim().split("\\s+");
				if(!InstrMap.toCode.keySet().contains(parts[0])) {
					error.append("\nError on line " + (lineNum+1) + ": illegal mnemonic");
					errorLine = lineNum;
				} else {
					if(!parts[0].equals(parts[0].toUpperCase())) {
						error.append("\nError on line" + (lineNum+1) + ": illegal mnemonic");
					} else {
						//ERROR 5
						if(noArgument.contains(parts[0])) {
							if(parts.length != 1) {
								error.append("\nError on line " + (lineNum+1) + ": this mnemonic cannot take arguments");
							}
						} else {
							if(parts.length > 2) {
								error.append("\nError on line " + (lineNum+1) + ": this mnemonic has too many arguments");
							} else if(parts.length < 2) {
								error.append("\nError on line " + (lineNum+1) + ": this mnemonic is missing an argument");
							}
							//ERROR 6
							try{
								@SuppressWarnings("unused")
								int arg = Integer.parseInt(parts[1],16);
							} catch(NumberFormatException e) {
								error.append("\nError on line " + (lineNum+1) + ": argument is not a hex number");
								errorLine = lineNum;	
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
						@SuppressWarnings("unused")
						int value = Integer.parseInt(parts[1],16);
					} catch(NumberFormatException e) {
						error.append("\nError on line " + (lineNum+1) + ": data has non-numeric memory address");
						errorLine = lineNum;
					}
				} else if(parts.length > 2){
					error.append("\nError on line " + (lineNum+1) + ": data has too many arguments");
					errorLine = lineNum;
				} else {
					error.append("\nError on line " + (lineNum+1) + ": data has too many arguments");
					errorLine = lineNum;
				}
			}
			lineNum++;
		}
		
		if(errorLine == 0) {
			System.out.println("Enter the name of the file without extension: ");
			int i = new SimpleAssembler().assemble(inputFileName, 
					outputFileName, error);
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
