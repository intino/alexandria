package io.intino.konos.alexandria.activity.box;

public class AlexandriaActivityBox extends AbstractBox {

	public AlexandriaActivityBox(String[] args) {
		super(args);
	}

	public AlexandriaActivityBox(AlexandriaActivityConfiguration configuration) {
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