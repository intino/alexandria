package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.spark.UISparkManager;

import java.util.function.Consumer;

public interface DisplayRouteManager {
	void get(String path, Consumer<UISparkManager> consumer);
	void post(String path, Consumer<UISparkManager> consumer);
}
