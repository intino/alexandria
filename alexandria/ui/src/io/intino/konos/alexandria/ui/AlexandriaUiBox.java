package io.intino.konos.alexandria.ui;

public class AlexandriaUiBox extends AbstractBox {

	public AlexandriaUiBox(String[] args) {
		super(args);
	}

	public AlexandriaUiBox(AlexandriaUiConfiguration configuration) {
		super(configuration);
	}

	@Override
	public io.intino.konos.alexandria.Box put(Object o) {
		super.put(o);
		return this;
	}

	public io.intino.konos.alexandria.Box open() {
		return super.open();
	}

	public void close() {
		super.close();
	}
}