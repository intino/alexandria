package io.intino.pandora.plugin.codegeneration.action;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.*;
import io.intino.pandora.plugin.Exception;
import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.object.ObjectData;
import io.intino.pandora.plugin.type.TypeData;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;


class ActionUpdater {

	private final Project project;
	private final String packageName;
	private final List<? extends Parameter> parameters;
	private final List<Exception> exceptions;
	private final TypeData returnType;
	private final PsiFile file;

	ActionUpdater(Project project, File destiny, String packageName, List<? extends Parameter> parameters, List<Exception> exceptions, TypeData returnType) {
		this.project = project;
		this.packageName = packageName;
		this.parameters = parameters;
		this.exceptions = exceptions;
		this.returnType = returnType;
		file = PsiManager.getInstance(project).findFile(VfsUtil.findFileByIoFile(destiny, true));
	}

	void update() {
		if (file == null || !(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		PsiJavaFile javaFile = (PsiJavaFile) file;
		final PsiClass psiClass = javaFile.getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed())
			runWriteCommandAction(project, () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		updateFields(psiClass);
		if (psiClass.getMethods().length > 0) {
			updateExceptions(psiClass.getMethods()[0]);
			updateReturnType(psiClass.getMethods()[0]);
		}
	}

	private void updateFields(PsiClass psiClass) {
		final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
		for (PsiField field : psiClass.getFields()) field.delete();
		for (Parameter parameter : parameters)
			psiClass.addAfter(createField(psiClass, elementFactory, parameter), psiClass.getLBrace().getNextSibling());
		if (!Arrays.stream(psiClass.getAllFields()).anyMatch(f -> "graph".equalsIgnoreCase(f.getName())))
			psiClass.addAfter(createGraphField(psiClass, elementFactory), psiClass.getLBrace().getNextSibling());
	}

	private void updateExceptions(PsiMethod psiMethod) {
		final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
		for (PsiJavaCodeReferenceElement element : psiMethod.getThrowsList().getReferenceElements()) element.delete();
		for (Exception exception : exceptions) {
			psiMethod.getThrowsList().add(elementFactory.createReferenceFromText(exception.owner().owner() == null ? exceptionReference(exception) : exception.code().name(), psiMethod));
		}
	}

	private String exceptionReference(Exception exception) {
		return packageName + ".exception." + Commons.firstUpperCase(exception.name());
	}

	private void updateReturnType(PsiMethod psiMethod) {
		final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
		psiMethod.getReturnTypeElement().replace(elementFactory.createTypeElement(elementFactory.createTypeFromText(formatType(returnType), psiMethod)));
	}

	private PsiField createField(PsiClass psiClass, PsiElementFactory elementFactory, Parameter parameter) {
		PsiField field = elementFactory.createField(parameter.name(), elementFactory.createTypeFromText(formatType(parameter.asType()), psiClass));
		if (field.getModifierList() == null) return field;
		field.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
		field.getModifierList().setModifierProperty(PsiModifier.PRIVATE, false);
		return field;
	}

	private PsiField createGraphField(PsiClass psiClass, PsiElementFactory elementFactory) {
		PsiField field = elementFactory.createField("graph", elementFactory.createTypeFromText("tara.magritte.Graph", psiClass));
		if (field.getModifierList() == null) return field;
		field.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
		field.getModifierList().setModifierProperty(PsiModifier.PRIVATE, false);
		return field;
	}

	private String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}
}
