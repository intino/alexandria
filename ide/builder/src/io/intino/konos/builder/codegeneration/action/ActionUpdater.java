package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.*;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.object.ObjectData;
import io.intino.konos.model.graph.type.TypeData;

import java.io.File;
import java.util.List;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static java.util.Arrays.stream;


class ActionUpdater {

	private final Project project;
	private final String packageName;
	private final List<? extends Parameter> parameters;
	private final List<Exception> exceptions;
	private final Response response;
	private final PsiFile file;

	ActionUpdater(Project project, File destiny, String packageName, List<? extends Parameter> parameters, List<Exception> exceptions, Response response) {
		this.project = project;
		this.packageName = packageName;
		this.parameters = parameters;
		this.exceptions = exceptions;
		this.response = response;
		file = PsiManager.getInstance(project).findFile(VfsUtil.findFileByIoFile(destiny, true));
	}

	void update() {
		if (!(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		final PsiClass psiClass = ((PsiJavaFile) file).getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed())
			runWriteCommandAction(project, () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		updateFields(psiClass);
		if (psiClass.getMethods().length > 0) {
			PsiMethod method = findExecuteMethod(psiClass);
			if (method == null) return;
			updateExceptions(method);
			updateReturnType(method);
		}
	}

	private PsiMethod findExecuteMethod(PsiClass psiClass) {
		for (PsiMethod method : psiClass.findMethodsByName("execute", false))
			if (!method.hasTypeParameters()) return method;
		return null;
	}

	private void updateFields(PsiClass psiClass) {
		final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
		parameters.stream().
				filter(parameter -> stream(psiClass.getAllFields()).noneMatch(f -> parameter.name$().equalsIgnoreCase(f.getName()))).
				forEach(parameter -> psiClass.addAfter(createField(psiClass, elementFactory, parameter), psiClass.getLBrace().getNextSibling()));
		if (stream(psiClass.getAllFields()).noneMatch(f -> "box".equalsIgnoreCase(f.getName())))
			psiClass.addAfter(createGraphField(psiClass, elementFactory), psiClass.getLBrace().getNextSibling());
	}

	private void updateExceptions(PsiMethod psiMethod) {
		final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
//		for (PsiJavaCodeReferenceElement element : psiMethod.getThrowsList().getReferenceElements()) element.delete();
		for (Exception exception : exceptions) {
			if (!hasException(psiMethod.getThrowsList().getReferenceElements(), exception))
				psiMethod.getThrowsList().add(elementFactory.createReferenceFromText(exception.core$().owner().owner() == null ? exceptionReference(exception) : exception.code().name(), psiMethod));
		}
	}

	private boolean hasException(PsiJavaCodeReferenceElement[] referenceElements, Exception exception) {
		for (PsiJavaCodeReferenceElement referenceElement : referenceElements)
			if (exception.code().name().equals(referenceElement.getReferenceName())) return true;
		return false;
	}

	private String exceptionReference(Exception exception) {
		return packageName + ".exceptions." + Commons.firstUpperCase(exception.name$());
	}

	private void updateReturnType(PsiMethod psiMethod) {
		final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
		psiMethod.getReturnTypeElement().replace(elementFactory.createTypeElement(elementFactory.createTypeFromText(response == null ? "void" : formatType(response.asType(), response.isList()), psiMethod)));
	}

	private PsiField createField(PsiClass psiClass, PsiElementFactory elementFactory, Parameter parameter) {
		PsiField field = elementFactory.createField(parameter.name$(), elementFactory.createTypeFromText(formatType(parameter.asType(), parameter.isList()), psiClass));
		if (field.getModifierList() == null) return field;
		field.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
		field.getModifierList().setModifierProperty(PsiModifier.PRIVATE, false);
		return field;
	}

	private PsiField createGraphField(PsiClass psiClass, PsiElementFactory elementFactory) {
		PsiField field = elementFactory.createField("box", elementFactory.createTypeFromText("io.intino.konos.Box", psiClass));
		if (field.getModifierList() == null) return field;
		field.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
		field.getModifierList().setModifierProperty(PsiModifier.PRIVATE, false);
		return field;
	}

	private String formatType(TypeData typeData, boolean list) {
		if (typeData == null || typeData.type() == null) return "void";
		final String type = (typeData.i$(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
		return list ? "List<" + type + ">" : type;
	}
}
