package io.intino.konos.builder.codegeneration.server.activity.dialog;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.Dialog;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.List;

public class DialogDisplayRenderer {


	private final Project project;
	private final File gen;
	private final String packageName;
	private final List<Dialog> displays;
	private final String boxName;

	public DialogDisplayRenderer(Project project, Graph graph, File gen, String packageName, String boxName) {
		this.project = project;
		this.gen = gen;
		this.packageName = packageName;
		this.displays = graph.find(Dialog.class);
		this.boxName = boxName;
	}

	public void execute() {
		displays.forEach(this::processDialog);
	}

	private void processDialog(Dialog dialog) {

	}
}
