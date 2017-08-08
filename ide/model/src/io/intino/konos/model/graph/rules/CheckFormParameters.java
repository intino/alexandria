package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.Parameter;
import io.intino.tara.lang.model.rules.NodeRule;

import java.util.stream.Collectors;

public class CheckFormParameters implements NodeRule {


	@Override
	public boolean accept(Node node) {
		int formParameters = 0;
		for (Node parameter : node.components().stream().filter(this::isParameter).collect(Collectors.toList())) {
			final Parameter in = parameter.parameters().stream().filter(p -> p.name().equals("in")).findFirst().orElse(null);
			if (in != null && !in.values().isEmpty() && in.values().get(0).toString().equals("form")) formParameters++;
		}
		return formParameters <= 1;
	}

	@Override
	public String errorMessage() {
		return "Only one form parameter is allowed";
	}

	private boolean isParameter(Node component) {
		return component.simpleType().equals("REST:Service.Resource.Parameter");
	}
}
