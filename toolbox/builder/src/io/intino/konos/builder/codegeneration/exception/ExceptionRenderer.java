package io.intino.konos.builder.codegeneration.exception;

import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;


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
		Frame frame = new Frame().addTypes("exception");
		frame.addSlot("name", exception.name$());
		frame.addSlot("code", exception.code());
		frame.addSlot("package", packageName);
		Commons.writeFrame(destinyPackage(gen), Commons.firstUpperCase(exception.name$()), template().format(frame));
	}

	private File destinyPackage(File destiny) {
		return new File(destiny, EXCEPTIONS);
	}

	private Template template() {
		return ExceptionTemplate.create();
	}


}
