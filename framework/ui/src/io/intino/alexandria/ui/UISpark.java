package io.intino.alexandria.ui;

import io.intino.alexandria.rest.AlexandriaSpark;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.spark.UIRouter;

public class UISpark extends AlexandriaSpark<UIRouter> {
	private AuthService authService;

	public UISpark(int port, AuthService authService) {
		this(port, WebDirectory, authService);
	}

	public UISpark(int port, String webDirectory, AuthService authService) {
		super(port, webDirectory);
		this.authService = authService;
	}

	public AuthService authService() {
		return authService;
	}

	public UISpark authService(AuthService authService) {
		this.authService = authService;
		return this;
	}

	public UISpark start() {
		return (UISpark) super.start();
	}

	@Override
	protected UIRouter createRouter(String path) {
		return new UIRouter(service, path, authService);
	}

}
