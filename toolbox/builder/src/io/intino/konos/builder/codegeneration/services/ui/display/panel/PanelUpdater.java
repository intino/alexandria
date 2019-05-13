package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Panel;

import java.io.File;

public class PanelUpdater extends Updater { // TODO Octavio
	public PanelUpdater(File file, Panel panel, Project project, String packageName, String box) {
		super(file, project, packageName, box);
	}

	@Override
	public void update() {

	}
	/*
	private Panel panel;
	private final Template template;

	public PanelUpdater(File sourceFile, Panel panel, Project project, String packageName, String box) {
		super(sourceFile, project, packageName, box);
		this.panel = panel;
		this.template = Formatters.customize(PanelSrcTemplate.create());
	}

	public void update() {
		if (file == null || !(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		final PsiClass psiClass = ((PsiJavaFile) file).getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed())
			runWriteCommandAction(project, () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		updateOperationMethods(psiClass);
		updateViewMethods(psiClass);
	}

	private void updateOperationMethods(PsiClass psiClass) {
		if (panel.toolbar() == null) return;
		PsiClass toolbar = innerClass(psiClass, "Toolbar");
		if (panel.toolbar().download() != null && toolbar.findMethodsByName(panel.toolbar().download().name$(), false).length == 0) {
			String text = methodFor(panel.toolbar().download());
			if (!text.isEmpty()) toolbar.addAfter(createMethodFromText(text), toolbar.getLBrace().getNextSibling());
		}
		if (panel.toolbar().export() != null && toolbar.findMethodsByName(panel.toolbar().export().name$(), false).length == 0) {
			String text = methodFor(panel.toolbar().export());
			if (!text.isEmpty()) toolbar.addAfter(createMethodFromText(text), toolbar.getLBrace().getNextSibling());
		}
		if (panel.toolbar().openDialog() != null && toolbar.findMethodsByName(panel.toolbar().openDialog().name$(), false).length == 0) {
			String text = methodFor(panel.toolbar().openDialog());
			if (!text.isEmpty()) toolbar.addAfter(createMethodFromText(text), toolbar.getLBrace().getNextSibling());
		}
		if (panel.toolbar().task() != null && toolbar.findMethodsByName(panel.toolbar().task().name$(), false).length == 0) {
			String text = methodFor(panel.toolbar().task());
			if (!text.isEmpty()) toolbar.addAfter(createMethodFromText(text), toolbar.getLBrace().getNextSibling());
		}
	}

	private String methodFor(Operation operation) {
		return template.format(frameOf(operation, panel, packageName));
	}

	private void updateViewMethods(PsiClass psiClass) {
		if (panel.views() == null) return;
		PsiClass views = innerClass(psiClass, "Views");
		List<View> viewList = new ArrayList<>(panel.views().viewList());
		Collections.reverse(viewList);
		for (View view : viewList) {
			if (exists(views, view) ||
					(view.elementRenderer().i$(RenderCatalogs.class) && !view.elementRenderer().a$(RenderCatalogs.class).filtered()))
				continue;
			String text = classFor(view);
			if (!text.isEmpty()) views.addAfter(createClass(text, psiClass), views.getLBrace().getNextSibling());
		}
	}

	private boolean exists(PsiClass views, View view) {
		return innerClass(views, firstUpperCase(view.name$())) != null;
	}

	private String classFor(View view) {
		return template.format(frameOf(view, panel, box));
	}
	*/
}
