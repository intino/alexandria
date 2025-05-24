package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.MogramRule;

public class CheckVirtual implements MogramRule {
	public boolean accept(Mogram m) {
		return m.appliedFacets().stream().noneMatch(a -> a.type().contains("Virtual")) ||
				(m.components().stream().noneMatch(c -> c.type().contains("Split")));
	}

	@Override
	public String errorMessage() {
		return "Virtual cubes cannot be splitted";
	}
}