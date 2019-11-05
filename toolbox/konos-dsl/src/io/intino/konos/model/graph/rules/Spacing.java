package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Rule;

public enum Spacing implements Rule<Enum> {
	DP8(8), DP16(16), DP24(24), DP32(32), DP40(40), None(0);

	private final int value;

	Spacing(int value) {
		this.value = value;
	}

	@Override
	public boolean accept(Enum value) {
		return value instanceof Spacing;
	}

	public int value() {
		return value;
	}
}
