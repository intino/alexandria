package io.intino.pandora.plugin.rules;

import tara.lang.model.EmptyNode;
import tara.lang.model.Node;
import tara.lang.model.Parameter;
import tara.lang.model.rules.composition.NodeRule;

import java.util.List;

import static io.intino.pandora.plugin.rules.CheckPath.Cause.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class CheckPath implements NodeRule {

	private Cause cause;

	public int min() {
		return 0;
	}

	public int max() {
		return Integer.MAX_VALUE;
	}

	public boolean accept(List<Node> nodes) {
		for (Node node : nodes)
			if (pathIsWrong(node)) return false;
		return true;
	}

	private boolean pathIsWrong(Node node) {
		if (parameter(node, "path") == null) return false;
		if (parameter(node, "path").values().get(0) instanceof EmptyNode) {
			cause = NullPath;
			return true;
		}
		return pathIsWrong((String) parameter(node, "path").values().get(0), node);
	}

	private boolean pathIsWrong(String pathValue, Node node) {
		List<String> parametersInPath = stream(pathValue.split("/")).filter(s -> s.startsWith(":")).map(s -> s.substring(1)).collect(toList());
		List<String> parametersDeclaredInPath = node.components().stream().filter(c -> isParameter(c) && parameterIsInPath(c)).map(Node::name).collect(toList());
		for (String parameterName : parametersInPath) {
			if (parametersDeclaredInPath.contains(parameterName)) continue;
			cause = ParameterNotDeclared;
			return true;
		}
		for (String parameterName : parametersDeclaredInPath) {
			if (parametersInPath.contains(parameterName)) continue;
			cause = ParameterNotInPath;
			return true;
		}
		return false;
	}

	private Parameter parameter(Node node, String name) {
		return node.parameters().stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
	}

	private boolean parameterIsInPath(Node parameter) {
		return "path".equals(parameter(parameter, "in").values().get(0).toString());
	}

	private boolean isParameter(Node component) {
		return component.types().contains("Resource.Parameter");
	}

	public String errorMessage() {
		if (cause == NullPath) return "Path cannot be empty";
		else if (cause == ParameterNotDeclared) return "Parameters in path must be declared as \"Parameter\"";
		else return "Declared parameter is not visible in resource's path";
	}

	enum Cause {NullPath, ParameterNotDeclared, ParameterNotInPath}

}
