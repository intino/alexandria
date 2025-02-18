package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.server.AlexandriaUiManager;

import java.util.function.Consumer;

public interface DisplayRouteManager {
	void get(String path, Consumer<AlexandriaUiManager> consumer);
	void post(String path, Consumer<AlexandriaUiManager> consumer);
	DisplayRouteDispatcher routeDispatcher();
}
