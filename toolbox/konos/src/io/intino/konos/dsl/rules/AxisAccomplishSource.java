package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.MogramRule;

public class AxisAccomplishSource implements MogramRule {
	public boolean accept(Mogram node) {
		return true;
	}

	@Override
	public String errorMessage() {
		return "A factor axis must have Category source. A distribution axis must have Numeric source";
	}
}
