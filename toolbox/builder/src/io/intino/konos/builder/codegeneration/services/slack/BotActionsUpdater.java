package io.intino.konos.builder.codegeneration.services.slack;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.compiler.shared.PostCompileMethodActionMessage;
import io.intino.konos.model.graph.Service.SlackBot.Request;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

class BotActionsUpdater {

	private final CompilationContext compilationContext;
	private final File destination;
	private final List<? extends Request> requests;

	BotActionsUpdater(CompilationContext compilationContext, File destination, List<? extends Request> requests) {
		this.compilationContext = compilationContext;
		this.destination = destination;
		this.requests = requests;
	}

	void update() {
		requests.forEach(this::addMethod);
	}

	private void addMethod(Request request) {
		compilationContext.postCompileActionMessages().add(new PostCompileMethodActionMessage(compilationContext.module(), destination, request.name$(), false, parameters(request), "String"));
	}

	private List<String> parameters(Request request) {
		return request.parameterList().stream().map(p -> p.type().name() + (p.multiple() ? "[]" : "") + " " + p.name$()).collect(Collectors.toList());
	}
}
