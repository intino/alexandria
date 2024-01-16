package io.intino.konos.model.rules;

import io.intino.tara.language.model.Rule;

public enum CodeLanguage implements Rule<Enum> {
	Html, Java, Javascript, R, Inl;

	@Override
	public boolean accept(Enum value) {
		return value instanceof CodeLanguage;
	}
}
