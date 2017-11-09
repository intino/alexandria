package io.intino.konos.alexandria.framework.box.model.renders;

import io.intino.konos.alexandria.foundation.activity.displays.Display;
import io.intino.konos.alexandria.framework.box.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.framework.box.model.ElementRender;

import java.util.function.Consumer;

public class RenderDisplay extends ElementRender {
	public boolean hideNavigator = true;
	private DisplayLoader displayLoader;

	public boolean hideNavigator() {
		return hideNavigator;
	}

	public RenderDisplay hideNavigator(boolean hideNavigator) {
		this.hideNavigator = hideNavigator;
		return this;
	}

	public Display display(Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {
		return displayLoader != null ? displayLoader.load(loadingListener, instantListener) : null;
	}

	public RenderDisplay displayLoader(DisplayLoader loader) {
		this.displayLoader = loader;
		return this;
	}

	public interface DisplayLoader {
		Display load(Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener);
	}
}
