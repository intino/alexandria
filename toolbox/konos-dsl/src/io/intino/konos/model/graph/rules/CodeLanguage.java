package io.intino.konos.model.graph.rules;

import io.intino.magritte.lang.model.Rule;

public enum CodeLanguage implements Rule<Enum> {
	Html, Java, Javascript, R, Inl;

	@Override
	public boolean accept(Enum value) {
		return value instanceof CodeLanguage;
	}
}
