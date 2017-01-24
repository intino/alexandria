package io.intino.konos.builder.codegeneration.process;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.Command;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.List;

public class CommandRenderer {

	private Project project;
	private final List<Command> commands;
	private final File gen;
	private final File src;
	private final String packageName;
	private final String boxName;

	public CommandRenderer(Project project, Graph graph, File gen, File src, String packageName, String boxName) {
		this.project = project;
		this.commands = graph.find(Command.class);
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
	}
}
