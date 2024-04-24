package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.MogramRule;

public class CheckVirtual implements MogramRule {
	public boolean accept(Mogram node) {
		return node.appliedFacets().stream().noneMatch(a -> a.type().contains("Virtual")) ||
				(node.components().stream().noneMatch(c -> c.type().contains("Split")));
	}

	@Override
	public String errorMessage() {
		return "Virtual cubers cannot be splitted";
	}
}