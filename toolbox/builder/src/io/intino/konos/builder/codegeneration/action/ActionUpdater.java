package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.compiler.shared.PostCompileFieldActionMessage;
import io.intino.konos.compiler.shared.PostCompileMethodActionMessage;
import io.intino.konos.model.graph.Data;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Response;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstLowerCase;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.returnType;


class ActionUpdater {

	private final CompilationContext compilationContext;
	private final File destination;
	private final String packageName;
	private final List<? extends Parameter> parameters;
	private final List<Exception> exceptions;
	private final Response response;

	ActionUpdater(CompilationContext compilationContext, File destination, String packageName, List<? extends Parameter> parameters, List<Exception> exceptions, Response response) {
		this.compilationContext = compilationContext;
		this.destination = destination;
		this.packageName = packageName;
		this.parameters = parameters;
		this.exceptions = exceptions;
		this.response = response;
	}

	void update() {
		parameters.forEach(p -> compilationContext.postCompileActionMessages().add(new PostCompileFieldActionMessage(compilationContext.module(), destination, "public",
				false, formatType(p.asType(), p.isList()), nameOf(p))));
		compilationContext.postCompileActionMessages().add(new PostCompileFieldActionMessage(compilationContext.module(), destination, "public",
				false, "io.intino.konos.Box", "box"));
		compilationContext.postCompileActionMessages().add(new PostCompileMethodActionMessage(compilationContext.module(), destination, "execute",
				false, Collections.emptyList(), response == null ? "void" : returnType(response, packageName), exceptions()));
	}

	private List<String> exceptions() {
		return exceptions.stream().map(e -> e.core$().owner().owner() == null ? exceptionReference(e) : e.code().name()).collect(Collectors.toList());
	}

	private String exceptionReference(Exception exception) {
		return packageName + ".exceptions." + Commons.firstUpperCase(exception.name$());
	}

	private String nameOf(Parameter parameter) {
		return firstLowerCase(snakeCaseToCamelCase().format(parameter.name$()).toString());
	}

	private String formatType(Data.Type typeData, boolean list) {
		if (typeData == null || typeData.type() == null) return "void";
		final String type = (typeData.i$(Data.Object.class) ? (packageName + ".schemas.") : "") + typeData.type();
		return list ? "List<" + type + ">" : type;
	}
}
