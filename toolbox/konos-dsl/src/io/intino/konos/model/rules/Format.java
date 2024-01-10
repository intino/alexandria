package io.intino.konos.model.rules;

import io.intino.tara.language.model.Rule;

public enum Format implements Rule<Enum> {
	H1, H2, H3, H4, H5, H6, Subtitle1, Subtitle2, Body1, Body2, Button, Caption, Overline, Default;

	@Override
	public boolean accept(Enum value) {
		return value instanceof Format;
	}
}
