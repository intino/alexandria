package io.intino.konos.jms.channel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public abstract class FileMessageReader extends MessageReader {

	private final BufferedReader reader;

	public FileMessageReader(File file) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(file));
		createPrototype();
	}

}
