package project;

import java.util.Set;
import java.util.TreeSet;

public interface Assembler {
	Set<String> noArgument = new TreeSet<String>();
	
	int assembler(String inputFileName, String outputFileName, StringBuilder error);
}
