package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.compiler.shared.PostCompileFieldActionMessage;
import io.intino.konos.compiler.shared.PostCompileMethodActionMessage;
import io.intino.konos.model.Exception;
import io.intino.konos.model.*;
import io.intino.konos.model.Service.REST.Resource.Operation;
import io.intino.magritte.framework.Node;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstLowerCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.codegeneration.services.rest.RESTResourceRenderer.RESOURCES_PACKAGE;


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
		String box = context.packageName() + "." + snakeCaseToCamelCase(context.boxName()) + "Box";
		parameters.forEach((key, value) -> context.postCompileActionMessages().add(new PostCompileFieldActionMessage(context.module(), destination, "public",
				false, formatType(value.asType(), value.isList()), nameOf(key))));
		context.postCompileActionMessages().add(new PostCompileFieldActionMessage(context.module(), destination, "public",
				false, box, "box"));
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
		return firstLowerCase(snakeCaseToCamelCase(parameter));
	}

	private String formatType(Data.Type typeData, boolean list) {
		if (typeData == null || typeData.type() == null) return "void";
		final String type;
		if (typeData.i$(Data.Object.class)) type = packageName + ".schemas." + typeData.type();
		else if (typeData.i$(Data.Word.class))
			type = owner(typeData) + "." + firstUpperCase(snakeCaseToCamelCase(typeData.name$()));
		else type = typeData.type();
		return list ? "List<" + type + ">" : type;
	}

	private String owner(Data.Type typeData) {
		Node node = typeData.core$();
		while (node != null && !node.is(Operation.class) && !node.is(Service.REST.Notification.class) && !node.is(Service.JMX.Operation.class)) {
			node = node.owner();
		}
		if (node == null) return typeData.type();
		if (node.is(Operation.class)) {
			Operation operation = node.as(Operation.class);
			return packageName + "." + RESOURCES_PACKAGE.replace("/", ".") + "." + firstUpperCase(name(node, operation)) + "Resource";
		}
		return node.name();
	}

	private static String name(Node node, Operation operation) {
		return snakeCaseToCamelCase(operation.getClass().getSimpleName() + "_" + node.ownerAs(Service.REST.Resource.class).name$());
	}
}
