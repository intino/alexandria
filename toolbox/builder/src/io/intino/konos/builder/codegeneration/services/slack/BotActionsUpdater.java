package io.intino.konos.builder.codegeneration.services.slack;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.SlackBot.Request;

import java.io.File;
import java.util.List;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;

class BotActionsUpdater {

	private final Project project;
	private final List<? extends Request> requests;
	private final PsiElementFactory factory;
	private final String boxName;
	private PsiFile file;

	BotActionsUpdater(Project project, File destination, List<? extends Request> requests, String boxName) {
		this.project = project;
		this.requests = requests;
		this.factory = JavaPsiFacade.getElementFactory(project);
		this.boxName = boxName;
		final VirtualFile vFile = VfsUtil.findFileByIoFile(destination, true);
		if (vFile != null) file = PsiManager.getInstance(project).findFile(vFile);
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
		for (Request request : requests) {
			final PsiMethod psiMethod = methodOf(request, psiClass);
			if (psiMethod != null) updateMethod(psiMethod, request);
			else addMethod(psiClass, request);
		}
	}

	private void addMethod(PsiClass psiClass, Request request) {
		final String methodText = Formatters.customize(new SlackTemplate()).render(createRequestFrame(request.core$().ownerAs(Service.SlackBot.class), request));
		psiClass.addAfter(factory.createMethodFromText(methodText, psiClass), anchor(psiClass));
	}

	private PsiElement anchor(PsiClass psiClass) {
		if (psiClass.getMethods().length == 0) return psiClass.getLBrace();
		PsiMethod returnMethod = null;
		for (PsiMethod psiMethod : psiClass.getMethods())
			if (psiMethod.getModifierList().hasExplicitModifier("private") && returnMethod != null) return returnMethod;
			else if (!psiMethod.getModifierList().hasExplicitModifier("private")) returnMethod = psiMethod;
		return psiClass.getMethods()[psiClass.getMethods().length - 1];
	}

	private Frame createRequestFrame(Service.SlackBot service, Service.SlackBot.Request request) {
		final FrameBuilder builder = new FrameBuilder("request", "newMethod").add("bot", service.name$()).add("box", boxName).add("name", request.name$()).add("description", request.description());
		final List<Service.SlackBot.Request.Parameter> parameters = request.parameterList();
		for (int i = 0; i < parameters.size(); i++) {
			final FrameBuilder parameterBuilder = new FrameBuilder("parameter", parameters.get(i).type().name()).
					add("type", parameters.get(i).type().name()).add("name", parameters.get(i).name$()).add("pos", i);
			if (parameters.get(i).multiple()) parameterBuilder.add("multiple");
			builder.add("parameter", parameterBuilder.toFrame());
		}
		return builder.toFrame();
	}

	private PsiMethod methodOf(Request request, PsiClass psiClass) {
		for (PsiMethod psiMethod : psiClass.getMethods())
			if (psiMethod.getName().equals(nameOf(request))) return psiMethod;
		return null;
	}

	private void updateMethod(PsiMethod psiMethod, Request request) {
		for (Request.Parameter parameter : request.parameterList()) {
			final PsiParameter psiParameter = parameter(psiMethod, parameter.name$());
			if (psiParameter != null) {
				final String presentableText = psiParameter.getTypeElement().getType().getPresentableText();
				if (!presentableText.equals(parameter.type().name() + (parameter.multiple() ? "[]" : "")) && !presentableText.equals(parameter.type().name() + (parameter.multiple() ? "..." : "")))
					psiParameter.replace(factory.createParameter(parameter.name$(), factory.createTypeFromText("java.lang." + parameter.type().name(), psiMethod)));
			} else
				psiMethod.getParameterList().add(factory.createParameter(parameter.name$(), factory.createTypeFromText("java.lang." + parameter.type().name(), psiMethod)));
		}
	}

	private String nameOf(Request request) {
		return Formatters.firstLowerCase(Formatters.snakeCaseToCamelCase().format(request.name$()).toString());
	}

	private PsiParameter parameter(PsiMethod method, String name) {
		for (PsiParameter parameter : method.getParameterList().getParameters())
			if (name.equals(parameter.getName())) return parameter;
		return null;
	}
}
