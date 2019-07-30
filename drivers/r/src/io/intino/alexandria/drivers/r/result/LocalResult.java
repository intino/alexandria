package io.intino.alexandria.drivers.r.result;

import io.intino.alexandria.drivers.r.Result;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LocalResult implements Result {
	private final File programDirectory;

	public LocalResult(File programDirectory) {
		this.programDirectory = programDirectory;
	}

	@Override
	public String getVariable(String name) {
		return null;
	}

	@Override
	public InputStream getFile(String filename) {
		try {
			return new FileInputStream(new File(programDirectory + "/" + filename));
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	@Override
	public void close() {
	}

}
