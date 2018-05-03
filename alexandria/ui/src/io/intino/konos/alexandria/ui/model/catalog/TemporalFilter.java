package io.intino.konos.alexandria.ui.model.catalog;

import io.intino.konos.alexandria.ui.model.TemporalCatalog;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class TemporalFilter {
	private EnabledLoader enabledLoader;
	private VisibilityLoader visibilityLoader;
	private Layout layout = Layout.Vertical;

	public enum Layout { Vertical, Horizontal }

	public boolean enabled(TemporalCatalog catalog, Scope scope, UISession session) {
		return enabledLoader == null || enabledLoader.enabled(catalog, scope, session);
	}

	public TemporalFilter enabledLoader(EnabledLoader enabledLoader) {
		this.enabledLoader = enabledLoader;
		return this;
	}

	public boolean visible(TemporalCatalog catalog, Scope scope, UISession session) {
		return visibilityLoader == null || visibilityLoader.visible(catalog, scope, session);
	}

	public TemporalFilter visibilityLoader(VisibilityLoader visibilityLoader) {
		this.visibilityLoader = visibilityLoader;
		return this;
	}

	public Layout layout() {
		return layout;
	}

	public TemporalFilter layout(String layout) {
		return layout(Layout.valueOf(layout));
	}

	public TemporalFilter layout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public interface EnabledLoader {
		boolean enabled(TemporalCatalog catalog, Scope scope, UISession session);
	}

	public interface VisibilityLoader {
		boolean visible(TemporalCatalog catalog, Scope scope, UISession session);
	}
}
