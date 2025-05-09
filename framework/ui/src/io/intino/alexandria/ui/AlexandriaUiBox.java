package io.intino.alexandria.ui;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.services.TranslatorService;
import io.intino.alexandria.ui.services.push.PushService;

import java.util.ArrayList;
import java.util.List;

public abstract class AlexandriaUiBox extends Box {
	private DisplayRouteManager router;
	protected TranslatorService translatorService;
	protected PushService pushService;
	protected List<Object> objectList = new ArrayList<>();

	public DisplayRouteManager routeManager() {
		return this.router;
	}

	public void routeManager(DisplayRouteManager router) {
		this.router = router;
	}

	public abstract void registerSoul(String clientId, Soul soul);
	public abstract void unRegisterSoul(String id);

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		return objectList.stream().filter(o -> clazz.isAssignableFrom(o.getClass())).map(o -> (T)o).findFirst().orElse(null);
	}

	@Override
	public io.intino.alexandria.core.Box put(Object o) {
		objectList.add(o);
		return this;
	}

	public TranslatorService translatorService() {
		return translatorService;
	}

	public PushService pushService() {
		return pushService;
	}

	protected AlexandriaUiBox pushService(PushService pushService) {
		this.pushService = pushService;
		return this;
	}

	public interface SoulsClosed {
		void accept();
	}
}
