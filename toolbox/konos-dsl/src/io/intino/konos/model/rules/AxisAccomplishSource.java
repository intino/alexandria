package io.intino.konos.model.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.NodeRule;

public class AxisAccomplishSource implements NodeRule {
	public boolean accept(Mogram node) {
		return true;
	}

	@Override
	public String errorMessage() {
		return "A factor axis must have Category source. A distribution axis must have Numeric source";
	}
}
