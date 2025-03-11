package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Rule;

public enum CodeLanguage implements Rule<Enum> {
	Html, Java, Javascript, R, Inl, Markdown;

	@Override
	public boolean accept(Enum value) {
		return value instanceof CodeLanguage;
	}
}
