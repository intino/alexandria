package io.intino.konos.model.rules;

import io.intino.tara.language.model.EmptyMogram;
import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.Parameter;
import io.intino.tara.language.model.rules.NodeRule;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class CheckPath implements NodeRule {

	private Cause cause;

	public boolean accept(Mogram mogram) {
		return !pathIsWrong(mogram);
	}

	private boolean pathIsWrong(Mogram mogram) {
		if (parameter(mogram, "path") == null) return false;
		if (parameter(mogram, "path").values().get(0) instanceof EmptyMogram) {
			cause = Cause.NullPath;
			return true;
		}
		return pathIsWrong((String) parameter(mogram, "path").values().get(0), mogram);
	}

	private boolean pathIsWrong(String pathValue, Mogram mogram) {
		if (mogram == null) return false;
		List<String> parametersInPath = stream(pathValue.split("/")).filter(s -> s.startsWith(":")).map(s -> s.substring(1)).collect(toList());
		List<String> parametersDeclaredInPath = pathParametersFromMogram(mogram);
		parametersDeclaredInPath.addAll(pathParametersInMethods(mogram.components()));
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

	private List<String> pathParametersFromMogram(Mogram node) {
		return node.components().stream().filter(c -> isParameter(c) && parameterIsInPath(c)).map(Mogram::name).collect(toList());
	}

	private List<String> pathParametersInMethods(List<Mogram> methods) {
		List<String> parameters = new ArrayList<>();
		for (Mogram component : methods) parameters.addAll(pathParametersFromMogram(component));
		return parameters;
	}

	private boolean parameterIsInPath(Mogram node) {
		return "path".equals(parameter(node, "in").values().get(0).toString());
	}

	private Parameter parameter(Mogram node, String name) {
		return node.parameters().stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
	}

	private boolean isParameter(Mogram component) {
		return component.type().equals("Service.REST.Resource.Parameter");
	}

	public String errorMessage() {
		if (cause == Cause.NullPath) return "Path cannot be empty";
		else if (cause == Cause.ParameterNotDeclared) return "Parameters in path must be declared as \"Parameter\"";
		else return "Declared parameter is not visible in resource's path";
	}

	enum Cause {NullPath, ParameterNotDeclared, ParameterNotInPath}

}
