package io.intino.alexandria.ui;

public class UiFrameworkBox extends AbstractBox {

	public UiFrameworkBox(String[] args) {
		super(args);
	}

	public UiFrameworkBox(UiFrameworkConfiguration configuration) {
		super(configuration);
	}

	@Override
	public io.intino.alexandria.Box put(Object o) {
		super.put(o);
		return this;
	}

	public io.intino.alexandria.Box open() {
		return super.open();
	}

	public void close() {
		super.close();
	}
}