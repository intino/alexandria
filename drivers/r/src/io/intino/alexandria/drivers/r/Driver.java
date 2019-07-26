package io.intino.alexandria.drivers.r;

import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.net.MalformedURLException;
import java.net.URL;

public class Driver implements io.intino.alexandria.drivers.Driver<URL, Result> {
	private final URL serverUrl;

	public static final String Script = "script";
	private static final int DefaultRServePort = 6311;

	public Driver(String serverUrl) {
		this.serverUrl = urlOf(serverUrl);
	}

	@Override
	public URL info(String program) {
		return null;
	}

	@Override
	public boolean isPublished(String program) {
		return false;
	}

	@Override
	public URL publish(io.intino.alexandria.drivers.Program program) {
		return null;
	}

	@Override
	public void update(io.intino.alexandria.drivers.Program program) {
	}

	@Override
	public void unPublish(String program) {
	}

	@Override
	public Result run(Program program) {

		if (program.scripts().size() <= 0) {
			Logger.error("R driver: No scripts defined in program");
			return null;
		}

		String host = serverUrl != null ? serverUrl.getHost() : "";
		int port = serverUrl != null ? serverUrl.getPort() : DefaultRServePort;

		try {
			RConnection connection = new RConnection(host, port);
			return RScriptBuilder.build(connection, program).run();
		} catch (RserveException e) {
			Logger.error("R driver: Could not connect with R server");
			return null;
		}
	}

	private URL urlOf(String serverUrl) {
		try {
			return new URL(serverUrl);
		} catch (MalformedURLException e) {
			Logger.error("R driver: invalid server url " + serverUrl);
			return null;
		}
	}

}
