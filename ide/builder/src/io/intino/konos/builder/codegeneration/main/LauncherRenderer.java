package io.intino.konos.builder.codegeneration.main;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class LauncherRenderer {

	private final File destination;
	private Frame frame;

	public LauncherRenderer(File destination, Frame frame) {
		this.destination = destination;
		this.frame = frame;
	}

	public void execute() {
		if (Commons.javaFile(destination, "Setup").exists()) return;
		Commons.writeFrame(destination, "Launcher", template().format(frame));
	}

	private Template template() {
		return Formatters.customize(LauncherTemplate.create());
	}

}
