package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.Parameter;
import io.intino.tara.lang.model.rules.NodeRule;

import java.util.List;
import java.util.stream.Collectors;

public class CheckFileParameter implements NodeRule {
	private Cause cause;


	private enum Cause {
		FileParameterNotInForm("File parameters only can be added in form");

		private final String message;

		Cause(String message) {
			this.message = message;
		}
	}


	@Override
	public boolean accept(Node node) {
		final List<Node> files = node.components().stream().filter(n -> isParameter(n) && n.appliedAspects().stream().anyMatch(f -> f.type().equals("File"))).collect(Collectors.toList());
		if (!files.isEmpty() && (! (parameterInForm(files.get(0)) || parameterInBody(files.get(0))))) {
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
		return component.type().equals("Service.REST.Resource.Parameter");
	}

	private boolean parameterInForm(Node node) {
		return "form".equals(parameter(node, "in").values().get(0).toString());
	}

	private boolean parameterInBody(Node node) {
		return "body".equals(parameter(node, "in").values().get(0).toString());
	}

	private Parameter parameter(Node node, String name) {
		return node.parameters().stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
	}
}