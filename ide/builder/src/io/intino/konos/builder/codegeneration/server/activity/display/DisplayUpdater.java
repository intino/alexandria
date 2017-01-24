package io.intino.konos.builder.codegeneration.server.activity.display;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.*;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.model.Activity.Display;
import io.intino.konos.model.Activity.Display.Request;

import java.io.File;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;

public class DisplayUpdater {
	private final PsiElementFactory factory;
	private final PsiFile file;
	private Project project;
	private Display display;

	public DisplayUpdater(Project project, Display display, File file) {
		this.project = project;
		this.factory = JavaPsiFacade.getElementFactory(project);
		this.display = display;
		this.file = PsiManager.getInstance(project).findFile(VfsUtil.findFileByIoFile(file, true));

	}


	public void update() {
		if (file == null || !(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		PsiJavaFile javaFile = (PsiJavaFile) file;
		final PsiClass psiClass = javaFile.getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed())
			runWriteCommandAction(project, () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		for (Request request : display.requestList())
			if (methodOf(request, psiClass) == null) addMethod(psiClass, request);
	}

	private void addMethod(PsiClass psiClass, Request request) {
		final String methodText = Formatters.customize(DisplayTemplate.create()).format(DisplayRenderer.frameOf(request));
		psiClass.addAfter(factory.createMethodFromText(methodText, psiClass), psiClass.getMethods()[psiClass.getMethods().length - 1]);
	}

	private PsiMethod methodOf(Request request, PsiClass psiClass) {
		for (PsiMethod psiMethod : psiClass.getMethods())
			if (psiMethod.getName().equals(nameOf(request))) return psiMethod;
		return null;
	}

	private String nameOf(Request request) {
		return Formatters.firstLowerCase(Formatters.snakeCaseToCamelCase().format(request.name()).toString());
	}
}
