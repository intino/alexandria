package io.intino.alexandria.drivers.r;

import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDriver implements io.intino.alexandria.drivers.Driver<URL, Result> {
	private final String host;
	private final int port;

	public static final String Script = "script";
	private static final int DefaultRServePort = 6311;

	public RemoteDriver() {
		this("", DefaultRServePort);
	}

	public RemoteDriver(String host) {
		this(host, DefaultRServePort);
	}

	public RemoteDriver(String host, int port) {
		this.host = host;
		this.port = port;
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

		String host = this.host != null ? this.host : "";
		int port = this.port != -1 ? this.port : DefaultRServePort;

		try {
			RConnection connection = new RConnection(host, port);
			return RScriptBuilder.build(connection, program).run();
		} catch (RserveException e) {
			Logger.error("R driver: Could not connect with R server");
			return null;
		}
	}

}
