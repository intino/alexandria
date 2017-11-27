package io.intino.konos.alexandria.activity.model.catalog.views;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.activity.model.catalog.View;

import java.util.function.Consumer;

public class DisplayView extends View {
	public boolean hideNavigator = true;
	private DisplayLoader displayLoader;

	public boolean hideNavigator() {
		return hideNavigator;
	}

	public DisplayView hideNavigator(boolean hideNavigator) {
		this.hideNavigator = hideNavigator;
		return this;
	}

	public AlexandriaDisplay display(Object context, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener, String username) {
		return displayLoader != null ? displayLoader.load(context, loadingListener, instantListener, username) : null;
	}

	public DisplayView displayLoader(DisplayLoader loader) {
		this.displayLoader = loader;
		return this;
	}

	public interface DisplayLoader {
		AlexandriaDisplay load(Object context, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener, String username);
	}
}
