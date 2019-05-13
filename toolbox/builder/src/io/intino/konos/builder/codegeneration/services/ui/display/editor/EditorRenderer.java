package io.intino.konos.builder.codegeneration.services.ui.display.editor;

import com.intellij.openapi.project.Project;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.services.ui.DisplayRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Editor;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.customize;

public class EditorRenderer extends DisplayRenderer {
	private final Project project;

	public EditorRenderer(Project project, Editor editor, String packageName, String box) {
		super(editor, box, packageName);
		this.project = project;
	}

	@Override
	public FrameBuilder frameBuilder() {
		final Editor editor = display().a$(Editor.class);
		FrameBuilder builder = super.frameBuilder();
		builder.add("display", editor.display().name$());
		return builder;
	}

	@Override
	protected Template srcTemplate() {
		return customize(new EditorTemplate());
	}

	@Override
	protected Template genTemplate() {
		return customize(new AbstractEditorTemplate());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new EditorUpdater(sourceFile, display().a$(Editor.class), project, packageName, box);
	}
}