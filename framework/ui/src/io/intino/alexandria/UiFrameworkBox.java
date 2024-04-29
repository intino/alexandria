package io.intino.alexandria;

public class UiFrameworkBox extends AbstractBox {

	public UiFrameworkBox(String[] args) {
		this(new UiFrameworkConfiguration(args));
	}

	public UiFrameworkBox(UiFrameworkConfiguration configuration) {
		super(configuration);
	}

	@Override
	public io.intino.alexandria.core.Box put(Object o) {
		super.put(o);
		return this;
	}

	public void beforeStart() {

	}

	public void afterStart() {

	}

	public void beforeStop() {

	}

	public void afterStop() {

	}

	protected io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl) {
		//TODO add your authService
		return null;
	}
}