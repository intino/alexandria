package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Rule;

public enum Layout implements Rule<Enum> {
	Horizontal, Vertical, Center, CenterJustified, Flexible, StartJustified, EndJustified;

	@Override
	public boolean accept(Enum value) {
		return value instanceof Layout;
	}
}
