package io.intino.alexandria.office.components;

/**
 * <p>Break line.</p>
 * */
public class Br extends Run {

	@Override
	public Br styles(StyleGroup styles) {
		return (Br) super.styles(styles);
	}

	@Override
	public Br styles(Style... styles) {
		return (Br) super.styles(styles);
	}

	@Override
	protected String getValueXML() {
		return "<w:br/>";
	}
}
