package io.intino.konos.builder.codegeneration.services.ui.display.toolbar;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.ui.Updater;

import java.io.File;

public class OperationUpdater extends Updater {

	public OperationUpdater(File file, Project project, String packageName, String box) {
		super(file, project, packageName, box);
	}

	@Override
	public void update() {

	}
/*
	private final Toolbar toolbar;
	private final Operation operation;
	private final Template template;

	public OperationUpdater(File sourceFile, Toolbar toolbar, Operation operation, Project project, String packageName, String box) {
		super(sourceFile, project, packageName, box);
		this.toolbar = toolbar;
		this.operation = operation;
		this.template = Formatters.customize(CatalogSrcTemplate.create());
	}

	public void update() {
		if (file == null || !(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		final PsiClass psiClass = ((PsiJavaFile) file).getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed()) runWriteCommandAction(project, () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		updateOperationMethods(psiClass);
	}

	private void updateOperationMethods(PsiClass psiClass) {
		final String operationType = operation.getClass().getSimpleName();
		final PsiClass operationPsiClass = innerClass(psiClass, toolbar.getClass().getSimpleName());
		final PsiMethod[] methods = operationPsiClass.findMethodsByName(firstLowerCase(operationType), false);
		if (methods.length == 0) {
			String text = operationMethodText(operation);
			if (text != null && !text.isEmpty()) operationPsiClass.add(createMethodFromText(text));
		}
	}

	private String operationMethodText(Operation operation) {
		return template.format(frameOf(operation, catalog, box, catalog.itemClass(), packageName));
	}
*/
}
