package io.intino.alexandria.drivers.r;

import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.drivers.program.Script;
import io.intino.alexandria.drivers.r.result.LocalResult;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static java.lang.ProcessBuilder.Redirect.INHERIT;

public class LocalDriver implements io.intino.alexandria.drivers.Driver<URL, Result> {
	private final File workingDirectory;

	public LocalDriver(File workingDirectory) {
		this.workingDirectory = workingDirectory;
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
	public URL publish(Program program) {
		return null;
	}

	@Override
	public void update(Program program) {
	}

	@Override
	public void unPublish(String program) {
	}

	@Override
	public Result run(Program program) {
		File workingDirectory = new File(this.workingDirectory + "/" + program.name());

		if (program.scripts().size() <= 0) {
			Logger.error("R driver: No scripts defined in program");
			return null;
		}

		copyResources(program, workingDirectory);
		copyScripts(program, workingDirectory);
		program.scripts().forEach(s -> runScript(workingDirectory, s));

		return new LocalResult(workingDirectory);
	}

	private ProcessBuilder runScript(File workingDirectory, Script s) {
		return new ProcessBuilder("Rscript", s.name()).directory(workingDirectory).redirectOutput(INHERIT).redirectError(INHERIT);
	}

	private void copyScripts(Program program, File workingDirectory) {
		program.scripts().forEach(r -> {
			try {
				Files.copy(r.content(), new File(workingDirectory + r.name()).toPath());
			} catch (IOException e) {
				Logger.error(e);
			}
		});
	}

	private void copyResources(Program program, File workingDirectory) {
		program.resources().forEach(r -> {
			try {
				Files.copy(r.content(), new File(workingDirectory + r.name()).toPath());
			} catch (IOException e) {
				Logger.error(e);
			}
		});
	}

}
