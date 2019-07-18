package io.intino.konos.builder.codegeneration.ui.displays;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.*;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.templates.DisplayTemplate;
import io.intino.konos.builder.codegeneration.ui.passiveview.PassiveViewRenderer;
import io.intino.konos.builder.codegeneration.ui.passiveview.PassiveViewUpdater;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView.Request;

import java.io.File;
import java.util.Objects;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;

public class DisplayUpdater<D extends Display> extends PassiveViewUpdater<D> {
	private final PsiElementFactory factory;
	private final PsiFile file;
	private Project project;
	private Display display;
	private String packageName;
	private Application application = ApplicationManager.getApplication();

	protected DisplayUpdater(Settings settings, D display, File file) {
		super(settings, display, file);
		this.project = settings.project();
		this.factory = JavaPsiFacade.getElementFactory(project);
		this.display = display;
		this.packageName = settings.packageName();
		this.file = application.runReadAction((Computable<PsiFile>) () -> PsiManager.getInstance(project).findFile(Objects.requireNonNull(VfsUtil.findFileByIoFile(file, true))));
	}

	public void update() {
		PsiClass psiClass = application.runReadAction((Computable<PsiClass>) () -> {
			if (!(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return null;
			return ((PsiJavaFile) file).getClasses()[0];
		});
		if (!application.isWriteAccessAllowed())
			runWriteCommandAction(project, () -> update(psiClass));
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
