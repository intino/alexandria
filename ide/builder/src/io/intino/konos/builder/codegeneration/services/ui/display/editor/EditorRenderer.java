package io.intino.konos.builder.codegeneration.services.ui.display.editor;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.DisplayRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Editor;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class EditorRenderer extends DisplayRenderer {
	private final Project project;

	public EditorRenderer(Project project, Editor editor, String packageName, String box) {
		super(editor, box, packageName);
		this.project = project;
	}

	@Override
	public Frame buildFrame() {
		final Editor editor = display().a$(Editor.class);
		Frame frame = super.buildFrame();
		frame.addSlot("display", editor.display().name$());
		return frame;
	}

	@Override
	protected Template srcTemplate() {
		return Formatters.customize(EditorTemplate.create());
	}

	@Override
	protected Template genTemplate() {
		return Formatters.customize(AbstractEditorTemplate.create());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new EditorUpdater(sourceFile, display().a$(Editor.class), project, packageName, box);
	}
}
