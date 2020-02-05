package io.intino.alexandria;

import io.intino.alexandria.ui.services.AuthService;

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

	@Override
	protected void beforeStart() {
	}

	@Override
	protected void afterStart() {
	}

	@Override
	protected void beforeStop() {
	}

	@Override
	protected void afterStop() {
	}

	public io.intino.alexandria.core.Box open() {
		return super.start();
	}

	public void close() {
		super.stop();
	}

	@Override
	protected AuthService authService(URL authServiceUrl) {
		return null;
	}

}