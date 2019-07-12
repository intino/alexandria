package io.intino.konos.builder.codegeneration.services.ui;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public abstract class Updater {
	protected final PsiFile file;
	protected final Project project;
	protected final PsiElementFactory factory;
	protected final String packageName;
	protected final String box;
	private Application application = ApplicationManager.getApplication();

	public Updater(File file, Project project, String packageName, String box) {
		this.file = project == null ? null : application.runReadAction((Computable<PsiFile>) () -> PsiManager.getInstance(project).findFile(Objects.requireNonNull(VfsUtil.findFileByIoFile(file, true))));
		this.project = project;
		this.packageName = packageName;
		this.box = box;
		this.factory = project == null ? null : JavaPsiFacade.getElementFactory(project);

	}

	public abstract void update();

	@NotNull
	protected PsiMethod createMethod(String name, String returnType) {
		final PsiMethod method = factory.createMethod(name, factory.createTypeFromText(returnType, null));
		method.getModifierList().setModifierProperty("static", true);
		return method;
	}

	@NotNull
	protected PsiClass createClass(String text, PsiClass context) {
		return factory.createClassFromText(text, context).getInnerClasses()[0];
	}


	@NotNull
	protected PsiClass createInnerClass(String name) {
		return factory.createClass(name);
	}

	@NotNull
	protected PsiMethod createMethodFromText(String text) {
		return factory.createMethodFromText(text, null);
	}

	protected PsiClass innerClass(PsiClass psiClass, String name) {
		return Arrays.stream(psiClass.getInnerClasses()).filter(c -> name.equals(c.getName())).findFirst().orElse(null);
	}
}
