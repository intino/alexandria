package io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import io.intino.konos.builder.codegeneration.services.activity.display.prototypes.LayoutRenderer;
import io.intino.konos.builder.codegeneration.services.activity.display.prototypes.LayoutTemplate;
import io.intino.konos.model.graph.ElementOption;
import io.intino.konos.model.graph.Group;
import io.intino.konos.model.graph.Layout;
import org.siani.itrules.Template;

import java.io.File;
import java.util.List;
import java.util.Stack;

import static com.intellij.openapi.application.ApplicationManager.getApplication;
import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static io.intino.konos.builder.codegeneration.services.activity.display.prototypes.PrototypeRenderer.customize;

public class LayoutUpdater extends Updater {
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
		if (!text.isEmpty()) psiClass.addAfter(createClass(text, psiClass), psiClass.getConstructors()[0].getNextSibling());
	}

	private PsiClass classOf(ElementOption elementOption) {
		Stack<String> identifiers = new Stack<>();
		ElementOption option = elementOption;
		PsiClass parent = psiClass;
		while (option.core$().owner().is(ElementOption.class)) identifiers.push(option.name$());
		while (!identifiers.empty()) {
			PsiClass child = innerClass(parent, identifiers.pop());
			if (child == null) return null;
			else parent = child;
		}
		return parent;
	}


	private String classFor(ElementOption elementOption) {
		return template.format(layoutRenderer.frameOf(elementOption));
	}

}
