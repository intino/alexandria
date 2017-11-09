package io.intino.konos.alexandria.activity;

public class AlexandriaFrameworkBox extends AbstractBox {

	public AlexandriaFrameworkBox(String[] args) {
		super(args);
	}

	public AlexandriaFrameworkBox(AlexandriaFrameworkConfiguration configuration) {
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