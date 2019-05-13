package io.intino.konos.builder.codegeneration.exception;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class ExceptionRenderer {
	private static final String EXCEPTIONS = "exceptions";
	private final List<io.intino.konos.model.graph.Exception> exceptions;
	private File gen;
	private String packageName;

	public ExceptionRenderer(KonosGraph graph, File gen, String packageName) {
		this.exceptions = graph.exceptionList();
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		exceptions.forEach(this::processException);
	}

	private void processException(io.intino.konos.model.graph.Exception exception) {
		writeFrame(destinyPackage(gen), firstUpperCase(exception.name$()), template().render(
				new FrameBuilder("exception")
						.add("name", exception.name$())
						.add("code", exception.code())
						.add("package", packageName).toFrame()));
	}

	private File destinyPackage(File destiny) {
		return new File(destiny, EXCEPTIONS);
	}

	private Template template() {
		return new ExceptionTemplate();
	}
}
