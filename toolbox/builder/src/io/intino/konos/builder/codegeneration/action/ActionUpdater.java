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
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstLowerCase;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;


public class ActionUpdater {

	private final CompilationContext context;
	private final File destination;
	private final String packageName;
	private final Map<String, ? extends Parameter> parameters;
	private final List<Exception> exceptions;
	private final Response response;

	public ActionUpdater(CompilationContext context, File destination, String packageName, Map<String, ? extends Parameter> parameters, List<Exception> exceptions, Response response) {
		this.context = context;
		this.destination = destination;
		this.packageName = packageName;
		this.parameters = parameters;
		this.exceptions = exceptions;
		this.response = response;
	}

	public void update() {
		parameters.entrySet().forEach(p -> context.postCompileActionMessages().add(new PostCompileFieldActionMessage(context.module(), destination, "public",
				false, formatType(p.getValue().asType(), p.getValue().isList()), nameOf(p.getKey()))));
		context.postCompileActionMessages().add(new PostCompileFieldActionMessage(context.module(), destination, "public",
				false, "io.intino.konos.Box", "box"));
		context.postCompileActionMessages().add(new PostCompileMethodActionMessage(context.module(), destination, "execute",
				false, Collections.emptyList(), response == null ? "void" : returnType(), exceptions()));
	}

	private String returnType() {
		String type = Commons.fullReturnType(response, packageName);
		return !type.contains(".") && !type.equals("void") ? "java.lang." + type : type;
	}

	private List<String> exceptions() {
		return exceptions.stream().map(e -> e.core$().owner().owner() == null ? exceptionReference(e) : e.code().name()).collect(Collectors.toList());
	}

	private String exceptionReference(Exception exception) {
		return packageName + ".exceptions." + Commons.firstUpperCase(exception.name$());
	}

	private String nameOf(String parameter) {
		return firstLowerCase(snakeCaseToCamelCase().format(parameter).toString());
	}

	private String formatType(Data.Type typeData, boolean list) {
		if (typeData == null || typeData.type() == null) return "void";
		final String type = (typeData.i$(Data.Object.class) ? (packageName + ".schemas.") : "") + typeData.type();
		return list ? "List<" + type + ">" : type;
	}
}
