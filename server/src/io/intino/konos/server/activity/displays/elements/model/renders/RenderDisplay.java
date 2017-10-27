package io.intino.konos.server.activity.displays.elements.model.renders;

import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.catalogs.CatalogInstantBlock;
import io.intino.konos.server.activity.displays.elements.model.ElementRender;
import io.intino.konos.server.activity.displays.layouts.model.ElementOption;

import java.util.function.Consumer;

public class RenderDisplay extends ElementRender {
	public boolean hideNavigator = true;
	private DisplayLoader displayLoader;

	public RenderDisplay(ElementOption option) {
		super(option);
	}

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
