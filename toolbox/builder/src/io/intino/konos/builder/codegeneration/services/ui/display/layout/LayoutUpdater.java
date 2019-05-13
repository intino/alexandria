package io.intino.konos.builder.codegeneration.services.ui.display.layout;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.ui.Updater;

import java.io.File;

public class LayoutUpdater extends Updater { // TODO Octavio

	public LayoutUpdater(File file, Project project, String packageName, String box) {
		super(file, project, packageName, box);
	}

	@Override
	public void update() {
	}

	/*
	private final LayoutRenderer layoutRenderer;
	private final Template template;
	private Layout layout;
	private PsiClass psiClass;

	public LayoutUpdater(File sourceFile, Layout layout, Project project, String packageName, String box) {
		super(sourceFile, project, packageName, box);
		this.layout = layout;
		this.layoutRenderer = new LayoutRenderer(project, layout, null, null, packageName, box);
		this.template = customize(LayoutTemplate.create());
		if (file != null && file instanceof PsiJavaFile && ((PsiJavaFile) file).getClasses().length != 0)
			psiClass = ((PsiJavaFile) file).getClasses()[0];
	}

	public void update() {
		if (psiClass == null) return;
		if (!getApplication().isWriteAccessAllowed()) runWriteCommandAction(project, this::updateLayout);
		else updateLayout();
	}

	private void updateLayout() {
		update(layout.elementOptionList());
	}

	private void update(List<? extends ElementOption> elementOptions) {
		for (ElementOption option : elementOptions) {
			if (classOf(option) == null) update(option);
			if (option.i$(Group.class)) update(option.a$(Group.class).optionsList());
		}
	}

	private void update(ElementOption option) {
		String text = classFor(option);
		PsiClass parentClass = parentClass(option);
		if (!text.isEmpty())
			parentClass.addAfter(createClass(text, parentClass), parentClass == psiClass ? psiClass.getConstructors()[0].getNextSibling() : parentClass.getLBrace());
	}

	private PsiClass classOf(ElementOption elementOption) {
		PsiClass parent = psiClass;
		Stack<String> identifiers = qn(elementOption);
		while (!identifiers.empty()) {
			PsiClass child = innerClass(parent, identifiers.pop());
			if (child == null) return null;
			else parent = child;
		}
		return parent;
	}

	private PsiClass parentClass(ElementOption elementOption) {
		PsiClass parent = psiClass;
		Stack<String> identifiers = qn(elementOption);
		while (!identifiers.empty()) {
			final String id = identifiers.pop();
			if (identifiers.isEmpty()) return parent;
			else parent = innerClass(parent, id);
		}
		return parent;
	}

	@NotNull
	private Stack<String> qn(ElementOption option) {
		Stack<String> identifiers = new Stack<>();
		identifiers.push(firstUpperCase(option.name$()));
		while (option.core$().owner().is(ElementOption.class)) {
			identifiers.push(firstUpperCase(option.core$().owner().name()));
			option = option.core$().ownerAs(ElementOption.class);
		}
		return identifiers;
	}

	private String classFor(ElementOption elementOption) {
		return template.format(layoutRenderer.frameOf(elementOption));
	}
	*/
}
