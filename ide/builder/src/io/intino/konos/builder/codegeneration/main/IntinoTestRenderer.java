package io.intino.konos.builder.codegeneration.main;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class IntinoTestRenderer {

	private final File destination;
	private Frame frame;

	public IntinoTestRenderer(File destination, Frame frame) {
		this.destination = destination;
		this.frame = frame;
	}

	public void execute() {
		if (Commons.javaFile(destination, "IntinoTest").exists()) return;
		Commons.writeFrame(destination, "IntinoTest", template().format(frame));
	}

	private Template template() {
		return Formatters.customize(IntinoTestTemplate.create());
	}

}
