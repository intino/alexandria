package io.intino.konos.builder.codegeneration.services.ui.display.editor;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Editor;

import java.io.File;

public class EditorUpdater extends Updater {

	public EditorUpdater(File file, Editor editor, Project project, String packageName, String box) {
		super(file, project, packageName, box);
	}

	@Override
	public void update() {
	}
}
