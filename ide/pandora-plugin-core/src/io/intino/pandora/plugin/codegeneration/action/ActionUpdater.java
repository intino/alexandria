package io.intino.pandora.plugin.codegeneration.action;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.*;
import io.intino.pandora.plugin.Parameter;
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
	private final PsiFile file;

	ActionUpdater(Project project, File destiny, String packageName, List<? extends Parameter> parameters) {
		this.project = project;
		this.packageName = packageName;
		this.parameters = parameters;
		file = PsiManager.getInstance(project).findFile(VfsUtil.findFileByIoFile(destiny, true));
	}

	void update() {
		if (file == null || !(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		PsiJavaFile javaFile = (PsiJavaFile) file;
		final PsiClass psiClass = javaFile.getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed())
			runWriteCommandAction(project, () -> updateFields(psiClass));
		else updateFields(psiClass);

	}

	private void updateFields(PsiClass psiClass) {
		final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
		for (PsiField field : psiClass.getFields()) field.delete();
		for (Parameter parameter : parameters)
			psiClass.addAfter(elementFactory.createField(parameter.name(), elementFactory.createTypeFromText(formatType(parameter.asType()), psiClass)), psiClass.getLBrace().getNextSibling());
		if (!Arrays.stream(psiClass.getAllFields()).anyMatch(f -> "graph".equalsIgnoreCase(f.getName())))
			psiClass.addAfter(elementFactory.createField("graph", elementFactory.createTypeFromText("tara.magritte.Graph", psiClass)), psiClass.getLBrace().getNextSibling());
	}

	private String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}
}
