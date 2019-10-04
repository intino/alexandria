package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Rule;

public enum Layout implements Rule<Enum> {
	Horizontal, HorizontalReverse, Vertical, VerticalReverse,
	Center, CenterJustified, CenterCenter,
	Flexible, Justified,
	Start, End, StartJustified, EndJustified, AroundJustified,
	Wrap, NoWrap, WrapReverse;

	@Override
	public boolean accept(Enum value) {
		return value instanceof Layout;
	}
}
