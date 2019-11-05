package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.EmptyNode;
import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.Parameter;
import io.intino.tara.lang.model.rules.NodeRule;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class CheckPath implements NodeRule {

	private Cause cause;

	public boolean accept(Node node) {
		return !pathIsWrong(node);
	}

	private boolean pathIsWrong(Node node) {
		if (parameter(node, "path") == null) return false;
		if (parameter(node, "path").values().get(0) instanceof EmptyNode) {
			cause = Cause.NullPath;
			return true;
		}
		return pathIsWrong((String) parameter(node, "path").values().get(0), node);
	}

	private boolean pathIsWrong(String pathValue, Node node) {
		if (node == null) return false;
		List<String> parametersInPath = stream(pathValue.split("/")).filter(s -> s.startsWith(":")).map(s -> s.substring(1)).collect(toList());
		List<String> parametersDeclaredInPath = pathParametersFromNode(node);
		parametersDeclaredInPath.addAll(pathParametersInMethods(node.components()));
		for (String parameterName : parametersInPath) {
			if (parametersDeclaredInPath.contains(parameterName)) continue;
			cause = Cause.ParameterNotDeclared;
			return true;
		}
		for (String parameterName : parametersDeclaredInPath) {
			if (parametersInPath.contains(parameterName)) continue;
			cause = Cause.ParameterNotInPath;
			return true;
		}
		return false;
	}

	private List<String> pathParametersFromNode(Node node) {
		return node.components().stream().filter(c -> isParameter(c) && parameterIsInPath(c)).map(Node::name).collect(toList());
	}

	private List<String> pathParametersInMethods(List<Node> methods) {
		List<String> parameters = new ArrayList<>();
		for (Node component : methods) parameters.addAll(pathParametersFromNode(component));
		return parameters;
	}

	private boolean parameterIsInPath(Node node) {
		return "path".equals(parameter(node, "in").values().get(0).toString());
	}

	private Parameter parameter(Node node, String name) {
		return node.parameters().stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
	}

	private boolean isParameter(Node component) {
		return component.type().equals("Service.REST.Resource.Parameter");
	}

	public String errorMessage() {
		if (cause == Cause.NullPath) return "Path cannot be empty";
		else if (cause == Cause.ParameterNotDeclared) return "Parameters in path must be declared as \"Parameter\"";
		else return "Declared parameter is not visible in resource's path";
	}

	enum Cause {NullPath, ParameterNotDeclared, ParameterNotInPath}

}
