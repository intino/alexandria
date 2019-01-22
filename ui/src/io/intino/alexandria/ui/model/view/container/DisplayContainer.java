package io.intino.alexandria.ui.model.view.container;

import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.displays.CatalogInstantBlock;
import io.intino.alexandria.ui.model.catalog.Scope;
import io.intino.alexandria.ui.services.push.UISession;

import java.util.function.Consumer;

public class DisplayContainer extends Container {
	public boolean hideNavigator = true;
	private DisplayLoader displayLoader;
	private ScopeManager scopeManager;

	public boolean hideNavigator() {
		return hideNavigator;
	}

	public DisplayContainer hideNavigator(boolean hideNavigator) {
		this.hideNavigator = hideNavigator;
		return this;
	}

	public AlexandriaDisplay display(Object context, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener, UISession session) {
		return displayLoader != null ? displayLoader.load(context, loadingListener, instantListener, session) : null;
	}

	public void update(AlexandriaDisplay display, Scope scope) {
		if (scopeManager == null) return;
		scopeManager.update(display, scope);
	}

	public DisplayContainer scopeManager(ScopeManager manager) {
		this.scopeManager = manager;
		return this;
	}

	public DisplayContainer displayLoader(DisplayLoader loader) {
		this.displayLoader = loader;
		return this;
	}

	public interface DisplayLoader {
		AlexandriaDisplay load(Object context, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener, UISession session);
	}

	public interface ScopeManager {
		void update(AlexandriaDisplay display, Scope scope);
	}
}
