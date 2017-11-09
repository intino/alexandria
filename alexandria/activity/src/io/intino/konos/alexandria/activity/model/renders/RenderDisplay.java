package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.activity.model.ElementRender;

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

	public AlexandriaDisplay display(Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {
		return displayLoader != null ? displayLoader.load(loadingListener, instantListener) : null;
	}

	public RenderDisplay displayLoader(DisplayLoader loader) {
		this.displayLoader = loader;
		return this;
	}

	public interface DisplayLoader {
		AlexandriaDisplay load(Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener);
	}
}
