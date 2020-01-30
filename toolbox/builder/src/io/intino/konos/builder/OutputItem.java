package io.intino.konos.builder;

public class OutputItem {
	private final String myOutputPath;
	private final String mySourceFileName;

	OutputItem(String sourceFileName, String outputFilePath) {
		myOutputPath = outputFilePath;
		mySourceFileName = sourceFileName;
	}

	public String getOutputPath() {
		return myOutputPath;
	}

	public String getSourceFile() {
		return mySourceFileName;
	}
}