package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Item;

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

	public AlexandriaDisplay display(Item target, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {
		return displayLoader != null ? displayLoader.load(target != null ? target.object() : null, loadingListener, instantListener) : null;
	}

	public RenderDisplay displayLoader(DisplayLoader loader) {
		this.displayLoader = loader;
		return this;
	}

	public interface DisplayLoader {
		AlexandriaDisplay load(Object target, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener);
	}

}
