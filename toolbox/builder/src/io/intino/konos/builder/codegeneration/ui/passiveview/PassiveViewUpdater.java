package io.intino.konos.builder.codegeneration.ui.passiveview;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.*;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.templates.DisplayTemplate;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.PassiveView.Request;

import java.io.File;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;

public class PassiveViewUpdater<C extends PassiveView> extends Updater {
	private final PsiElementFactory factory;
	protected C element;

	public PassiveViewUpdater(Settings settings, C element, File file) {
		super(settings, file);
		this.factory = JavaPsiFacade.getElementFactory(settings.project());
		this.element = element;
	}

	public void update() {
		if (!(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		PsiJavaFile javaFile = (PsiJavaFile) file;
		final PsiClass psiClass = javaFile.getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed())
			runWriteCommandAction(settings.project(), () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		for (Request request : element.requestList())
			if (methodOf(request, psiClass) == null) addMethod(psiClass, request);
	}

	private void addMethod(PsiClass psiClass, Request request) {
		final String methodText = Formatters.customize(new DisplayTemplate()).render(PassiveViewRenderer.frameOf(element, request, settings.packageName()));
		psiClass.addAfter(factory.createMethodFromText(methodText, psiClass), psiClass.getMethods()[psiClass.getMethods().length - 1]);
	}

	private PsiMethod methodOf(Request request, PsiClass psiClass) {
		for (PsiMethod psiMethod : psiClass.getMethods())
			if (psiMethod.getName().equals(nameOf(request))) return psiMethod;
		return null;
	}

	private String nameOf(Request request) {
		return Formatters.firstLowerCase(Formatters.snakeCaseToCamelCase().format(request.name$()).toString());
	}
}
