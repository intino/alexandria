package io.intino.alexandria.drivers.r;

import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.logger.Logger;
import org.apache.commons.io.IOUtils;
import org.rosuda.REngine.Rserve.RConnection;

import java.io.IOException;

public class RScriptBuilder {

	public static RScript build(RConnection connection, Program program) {
		RScript result = new RScript(connection);
		addLines(program, result);
		addResources(program, result);
		return result;
	}

	private static void addLines(Program program, RScript result) {
		program.scripts().forEach(s -> {
			try {
				result.add(IOUtils.toString(s.content()));
			} catch (IOException e) {
				Logger.error("R driver: Could not add script file in R", e);
			}
		});
	}

	private static void addResources(Program program, RScript result) {
		program.resources().forEach(r -> result.addFile(r.content(), r.name()));
	}

}
