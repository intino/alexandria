package io.intino.pandora.plugin.codegeneration.server.slack;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.*;
import io.intino.pandora.model.slackbot.SlackBotService;
import io.intino.pandora.model.slackbot.SlackBotService.Request;
import io.intino.pandora.plugin.codegeneration.Formatters;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static io.intino.pandora.plugin.codegeneration.Formatters.firstLowerCase;
import static io.intino.pandora.plugin.codegeneration.Formatters.snakeCaseToCamelCase;


class BotUpdater {

	private final Project project;
	private final List<? extends Request> requests;
	private final PsiFile file;
	private final PsiElementFactory factory;
	private final String boxName;

	BotUpdater(Project project, File destiny, List<? extends Request> requests, String boxName) {
		this.project = project;
		this.requests = requests;
		this.factory = JavaPsiFacade.getElementFactory(project);
		this.boxName = boxName;
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
		for (Request request : requests) {
			final PsiMethod psiMethod = methodOf(request, psiClass);
			if (psiMethod != null) updateMethod(psiMethod, request);
			else addMethod(psiClass, request);
		}
	}

	private void addMethod(PsiClass psiClass, Request request) {
		final String methodText = Formatters.customize(SlackTemplate.create()).format(createRequestFrame(request.owner().as(SlackBotService.class), request));
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

	private Frame createRequestFrame(SlackBotService service, SlackBotService.Request request) {
		final Frame requestFrame = new Frame().addTypes("request", "newMethod").addSlot("bot", service.name()).addSlot("box", boxName).addSlot("name", request.name()).addSlot("description", request.description());
		final List<SlackBotService.Request.Parameter> parameters = request.parameterList();
		for (int i = 0; i < parameters.size(); i++)
			requestFrame.addSlot("parameter", new Frame().addTypes("parameter", parameters.get(i).type().name()).
					addSlot("type", parameters.get(i).type().name()).addSlot("name", parameters.get(i).name()).addSlot("pos", i));
		return requestFrame;
	}

	private PsiMethod methodOf(Request request, PsiClass psiClass) {
		for (PsiMethod psiMethod : psiClass.getMethods())
			if (psiMethod.getName().equals(nameOf(request))) return psiMethod;
		return null;
	}

	private void updateMethod(PsiMethod psiMethod, Request request) {
		for (Request.Parameter parameter : request.parameterList()) {
			final PsiParameter psiParameter = parameter(psiMethod, parameter.name());
			if (psiParameter != null) {
				if (!psiParameter.getTypeElement().getType().getPresentableText().equals(parameter.type().name()))
					psiParameter.replace(factory.createParameter(parameter.name(), factory.createTypeFromText("java.lang." + parameter.type().name(), psiMethod)));
			} else
				psiMethod.getParameterList().add(factory.createParameter(parameter.name(), factory.createTypeFromText("java.lang." + parameter.type().name(), psiMethod)));
		}
	}

	private String nameOf(Request request) {
		return firstLowerCase(snakeCaseToCamelCase().format(request.name()).toString());
	}

	private PsiParameter parameter(PsiMethod method, String name) {
		for (PsiParameter parameter : method.getParameterList().getParameters()) if (name.equals(parameter.getName())) return parameter;
		return null;
	}
}
