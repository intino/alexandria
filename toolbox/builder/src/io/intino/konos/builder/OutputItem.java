package io.intino.konos.builder;

public class OutputItem {
	private final String myOutputPath;

	public OutputItem(String outputFilePath) {
		myOutputPath = outputFilePath;
	}

	public String getOutputPath() {
		return myOutputPath;
	}

	public String getSourceFile() {
		return null;
	}
}