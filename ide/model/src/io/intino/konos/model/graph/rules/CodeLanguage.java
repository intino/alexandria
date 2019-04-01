package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Rule;

public enum CodeLanguage implements Rule<Enum> {
	Html, Java, Javascript, R;

	@Override
	public boolean accept(Enum value) {
		return value instanceof CodeLanguage;
	}
}
