package io.intino.alexandria.drivers.r.result;

import io.intino.alexandria.drivers.r.Result;
import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import java.io.IOException;
import java.io.InputStream;

public class RemoteResult implements Result {
	private final org.rosuda.REngine.Rserve.RConnection connection;
	private REXP expression;

	public RemoteResult(org.rosuda.REngine.Rserve.RConnection connection, REXP expression) {
		this.connection = connection;
		this.expression = expression;
	}

	@Override
	public String getVariable(String name) {
		try {
			if (expression == null) return null;
			return connection.get(name, expression, true).asString();
		} catch (REngineException | REXPMismatchException e) {
			Logger.error(e);
			return null;
		}
	}

	@Override
	public InputStream getFile(String filename) {
		try {
			return connection.openFile(filename);
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	public void close() {
		if (connection != null) connection.close();
	}

}
