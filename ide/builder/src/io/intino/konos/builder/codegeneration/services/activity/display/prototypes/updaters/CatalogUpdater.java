package io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.model.graph.Catalog;
import io.intino.konos.model.graph.Operation;
import io.intino.konos.model.graph.TemporalCatalog;

import java.io.File;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static io.intino.konos.builder.codegeneration.Formatters.firstLowerCase;
import static io.intino.konos.builder.codegeneration.services.activity.display.prototypes.PrototypeRenderer.shortType;

public class CatalogUpdater extends Updater {
	private Catalog catalog;

	public CatalogUpdater(File sourceFile, Catalog catalog, Project project, String packageName, String box) {
		super(sourceFile, project, packageName, box);
		this.catalog = catalog;
	}

	public void update() {
		if (file == null || !(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		final PsiClass psiClass = ((PsiJavaFile) file).getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed()) runWriteCommandAction(project, () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		if (catalog.i$(TemporalCatalog.class)) updateTemporalMethods(psiClass);
		updateToolbarMethods(psiClass);
	}

	private void updateTemporalMethods(PsiClass psiClass) {
		PsiClass sources = innerClass(psiClass, "Sources");
		PsiClass temporal = innerClass(psiClass, "Temporal");
		if (temporal == null) psiClass.addAfter(createClass(temporalClass()), constructorOf(psiClass));
		final String createdMethod = firstLowerCase(shortType(catalog.modelClass())) + "Created";
		if (sources.findMethodsByName(createdMethod, false).length == 0)
			sources.addAfter(createMethod(createdMethod, "java.time.Instant"), constructorOf(psiClass));

	}

	private void updateToolbarMethods(PsiClass psiClass) {
		if (catalog.toolbar() == null) return;
		PsiClass toolbar = innerClass(psiClass, "Toolbar");
		if (toolbar == null)
			toolbar = (PsiClass) psiClass.addBefore(createClass(temporalClass()), innerClass(psiClass, "Sources").getPrevSibling());
		for (Operation operation : catalog.toolbar().operations()) {
			final String operationType = operation.getClass().getSimpleName();
			final PsiMethod[] methods = toolbar.findMethodsByName(firstLowerCase(operationType), false);
			if (methods.length == 0) toolbar.add(createMethodFromText("public static void opera"));
		}
	}

	private PsiElement constructorOf(PsiClass psiClass) {
		return psiClass.getConstructors()[psiClass.getConstructors().length - 1].getNextSibling();
	}

	private String temporalClass() {
		return "public static class Temporal { \npublic static io.intino.konos.alexandria.activity.model.TimeRange range (" + Formatters.firstUpperCase(this.box) + "Box box, String username){\nreturn null;\n}\n}";
	}
}
