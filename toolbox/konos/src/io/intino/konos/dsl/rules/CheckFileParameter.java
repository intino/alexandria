package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.Parameter;
import io.intino.tara.language.model.rules.MogramRule;

import java.util.List;

public class CheckFileParameter implements MogramRule {
	private Cause cause;


	private enum Cause {
		FileParameterNotInForm("File parameters only can be added in form");

		private final String message;

		Cause(String message) {
			this.message = message;
		}
	}


	@Override
	public boolean accept(Mogram node) {
		final List<Mogram> files = node.components().stream().filter(n -> isParameter(n) && n.appliedFacets().stream().anyMatch(f -> f.type().equals("File"))).toList();
		if (!files.isEmpty() && (!(parameterInForm(files.get(0)) || parameterInBody(files.get(0))))) {
			cause = Cause.FileParameterNotInForm;
			return false;
		}
		return true;
	}

	@Override
	public String errorMessage() {
		return cause.message;
	}

	private boolean isParameter(Mogram component) {
		return component.type().equals("Service.REST.Resource.Parameter");
	}

	private boolean parameterInForm(Mogram node) {
		return "form".equals(parameter(node, "in").values().get(0).toString());
	}

	private boolean parameterInBody(Mogram node) {
		return "body".equals(parameter(node, "in").values().get(0).toString());
	}

	private Parameter parameter(Mogram node, String name) {
		return node.parameters().stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
	}
}