package io.intino.alexandria.drivers.r;

import java.io.IOException;
import java.io.InputStream;

public class Result {
	private final org.rosuda.REngine.Rserve.RConnection connection;

	public Result(org.rosuda.REngine.Rserve.RConnection connection) {
		this.connection = connection;
	}

	public InputStream get(String file) throws IOException {
		return connection.openFile(file);
	}

	public void close() {
		if (connection != null) connection.close();
	}

}
