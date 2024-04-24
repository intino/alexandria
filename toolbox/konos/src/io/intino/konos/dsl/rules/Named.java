package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.MogramRule;
import io.intino.tara.language.model.rules.MogramRule;

public class Named implements MogramRule {

	public boolean accept(Mogram mogram) {
		return !mogram.isAnonymous();
	}

	public String errorMessage() {
		return "This element must have name";
	}

}
