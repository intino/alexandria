package io.intino.alexandria.drivers.r;

import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import java.io.IOException;
import java.io.InputStream;

public class Result {
	private final org.rosuda.REngine.Rserve.RConnection connection;
	private REXP expression;

	public Result(org.rosuda.REngine.Rserve.RConnection connection, REXP expression) {
		this.connection = connection;
		this.expression = expression;
	}

	public String getVariable(String name) {
		try {
			if (expression == null) return null;
			return connection.get(name, expression, true).asString();
		} catch (REngineException | REXPMismatchException e) {
			Logger.error(e);
			return null;
		}
	}

	public InputStream getFile(String filename) throws IOException {
		return connection.openFile(filename);
	}

	public void close() {
		if (connection != null) connection.close();
	}

}
