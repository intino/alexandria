package io.intino.konos.alexandria.activity.model.catalog.views;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.activity.model.catalog.Scope;
import io.intino.konos.alexandria.activity.model.catalog.View;
import io.intino.konos.alexandria.activity.services.push.User;

import java.util.function.Consumer;

public class DisplayView extends View {
	public boolean hideNavigator = true;
	private DisplayLoader displayLoader;
	private ScopeManager scopeManager;

	public boolean hideNavigator() {
		return hideNavigator;
	}

	public DisplayView hideNavigator(boolean hideNavigator) {
		this.hideNavigator = hideNavigator;
		return this;
	}

	public AlexandriaDisplay display(Object context, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener, User user) {
		return displayLoader != null ? displayLoader.load(context, loadingListener, instantListener, user) : null;
	}

	public void update(AlexandriaDisplay display, Scope scope) {
		if (scopeManager == null) return;
		scopeManager.update(display, scope);
	}

	public DisplayView scopeManager(ScopeManager manager) {
		this.scopeManager = manager;
		return this;
	}

	public DisplayView displayLoader(DisplayLoader loader) {
		this.displayLoader = loader;
		return this;
	}

	public interface DisplayLoader {
		AlexandriaDisplay load(Object context, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener, User user);
	}

	public interface ScopeManager {
		void update(AlexandriaDisplay display, Scope scope);
	}
}
