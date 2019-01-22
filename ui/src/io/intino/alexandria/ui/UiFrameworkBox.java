package io.intino.alexandria.ui;

import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.EditorService;

import java.net.URL;

public class UiFrameworkBox extends AbstractBox {

	public UiFrameworkBox(String[] args) {
		super(args);
	}

	public UiFrameworkBox(UiFrameworkConfiguration configuration) {
		super(configuration);
	}

	@Override
	public io.intino.alexandria.core.Box put(Object o) {
		super.put(o);
		return this;
	}

	public io.intino.alexandria.core.Box open() {
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