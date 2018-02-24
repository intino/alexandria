package io.intino.konos.alexandria.activity.model.catalog;

import io.intino.konos.alexandria.activity.model.TemporalCatalog;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class TemporalFilter {
	private EnabledLoader enabledLoader;
	private VisibilityLoader visibilityLoader;

	public boolean enabled(TemporalCatalog catalog, Scope scope, ActivitySession session) {
		return enabledLoader == null || enabledLoader.enabled(catalog, scope, session);
	}

	public TemporalFilter enabledLoader(EnabledLoader enabledLoader) {
		this.enabledLoader = enabledLoader;
		return this;
	}

	public boolean visible(TemporalCatalog catalog, Scope scope, ActivitySession session) {
		return visibilityLoader == null || visibilityLoader.visible(catalog, scope, session);
	}

	public TemporalFilter visibilityLoader(VisibilityLoader visibilityLoader) {
		this.visibilityLoader = visibilityLoader;
		return this;
	}

	public interface EnabledLoader {
		boolean enabled(TemporalCatalog catalog, Scope scope, ActivitySession session);
	}

	public interface VisibilityLoader {
		boolean visible(TemporalCatalog catalog, Scope scope, ActivitySession session);
	}
}
