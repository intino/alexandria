package io.intino.konos.model.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.NodeRule;

public class CheckVirtual implements NodeRule {
	public boolean accept(Mogram node) {
		return node.appliedFacets().stream().noneMatch(a -> a.type().contains("Virtual")) ||
				(node.components().stream().noneMatch(c -> c.type().contains("Split")));
	}

	@Override
	public String errorMessage() {
		return "Virtual cubers cannot be splitted";
	}
}