package project;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class FullAssembler implements Assembler {

	@Override
	public int assembler(String inputFileName, String outputFileName, StringBuilder error) {
		Scanner reader = new Scanner(System.in);
		ArrayList<String> fileIn = new ArrayList<String>();
		while(reader.hasNext()) {
			fileIn.add(reader.nextLine());
		}
		return 0;
	}

}
