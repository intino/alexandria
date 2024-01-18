package io.intino.konos.model.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.NodeRule;

public class Named implements NodeRule {

	public boolean accept(Mogram mogram) {
		return !mogram.isAnonymous();
	}

	public String errorMessage() {
		return "This element must have name";
	}

}
