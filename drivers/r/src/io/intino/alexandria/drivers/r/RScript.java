package io.intino.alexandria.drivers.r;

import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RFileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static io.intino.alexandria.drivers.r.StreamHelper.copy;

public class RScript {
	private final org.rosuda.REngine.Rserve.RConnection connection;

	public RScript(org.rosuda.REngine.Rserve.RConnection connection) {
		this.connection = connection;
	}

	public void add(String... lines) {
		Stream.of(lines).forEach(line -> {
			try {
				connection.parseAndEval(line);
			} catch (REngineException | REXPMismatchException e) {
				Logger.error("Could not add script line", e);
			}
		});
	}

	public void addFile(InputStream content, String name) {
		try {
			RFileOutputStream serverStream = connection.createFile(name);
			copy(content, serverStream);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public Result run() {
		return new Result(connection);
	}

}
