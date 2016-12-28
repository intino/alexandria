package io.intino.pandora.model.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.Parameter;
import io.intino.tara.lang.model.rules.NodeRule;

import java.util.List;
import java.util.stream.Collectors;

public class CheckFileParameter implements NodeRule {
	private Cause cause;


	private enum Cause {
		MultipleFileParameter("Only one File parameter is allowed"),
		FileParameterNotInForm("File parameters only can be added in form");

		private final String message;

		Cause(String message) {
			this.message = message;
		}
	}


	@Override
	public boolean accept(Node node) {
		final List<Node> files = node.components().stream().filter(n -> isParameter(n) && n.facets().stream().anyMatch(f -> f.type().equals("File"))).collect(Collectors.toList());
		if (files.size() > 1) {
			cause = Cause.MultipleFileParameter;
			return false;
		} else if (!files.isEmpty() && !parameterInForm(files.get(0))) {
			cause = Cause.FileParameterNotInForm;
			return false;
		}
		return true;
	}

	@Override
	public String errorMessage() {
		return cause.message;
	}

	private boolean isParameter(Node component) {
		return component.simpleType().equals("REST:Service.Resource.Parameter");
	}

	private boolean parameterInForm(Node node) {
		return "form".equals(parameter(node, "in").values().get(0).toString());
	}

	private Parameter parameter(Node node, String name) {
		return node.parameters().stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
	}
}
