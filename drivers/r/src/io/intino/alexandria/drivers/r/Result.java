package io.intino.alexandria.drivers.r;

import org.rosuda.REngine.Rserve.RFileInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static io.intino.alexandria.drivers.r.StreamHelper.copy;

public class Result {
	private final org.rosuda.REngine.Rserve.RConnection connection;

	public Result(org.rosuda.REngine.Rserve.RConnection connection) {
		this.connection = connection;
	}

	public ByteArrayInputStream getFile(String file) throws IOException {
		RFileInputStream serverStream = connection.openFile(file);
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		copy(serverStream, result);
		return new ByteArrayInputStream(result.toByteArray());
	}

	public void close() {
		if (connection != null) connection.close();
	}

}
