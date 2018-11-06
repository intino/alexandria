package io.intino.alexandria.ui;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.EditorService;

import java.net.URL;

public class UiBox extends AbstractBox {

	public UiBox(String[] args) {
		super(args);
	}

	public UiBox(UiConfiguration configuration) {
		super(configuration);
	}

	@Override
	public Box put(Object o) {
		super.put(o);
		return this;
	}

	public Box open() {
		return super.open();
	}

	public void close() {
		super.close();
	}

	@Override
	protected AuthService authService(URL authServiceUrl) {
		return null;
	}

	@Override
	protected EditorService editorService(URL editorServiceUrl) {
		return null;
	}
}