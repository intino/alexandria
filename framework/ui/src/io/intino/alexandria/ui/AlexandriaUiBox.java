package io.intino.alexandria.ui;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.services.TranslatorService;

public abstract class AlexandriaUiBox extends Box {
	private DisplayRouteManager router;
	protected TranslatorService translatorService;

	public DisplayRouteManager routeManager() {
		return this.router;
	}

	public void routeManager(DisplayRouteManager router) {
		this.router = router;
	}

	public abstract void registerSoul(String clientId, Soul soul);
	public abstract void unRegisterSoul(String id);

	public TranslatorService translatorService() {
		return translatorService;
	}

	public interface SoulsClosed {
		void accept();
	}
}
