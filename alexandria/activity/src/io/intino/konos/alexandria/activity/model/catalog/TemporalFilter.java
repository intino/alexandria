package io.intino.konos.alexandria.activity.model.catalog;

import io.intino.konos.alexandria.activity.model.TemporalCatalog;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class TemporalFilter {
	private EnabledLoader enabledLoader;
	private VisibilityLoader visibilityLoader;
	private Layout layout = Layout.HorizontalTimeLine;

	public enum Layout { VerticalTimeLine, HorizontalTimeLine }

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
		boolean enabled(TemporalCatalog catalog, Scope scope, ActivitySession session);
	}

	public interface VisibilityLoader {
		boolean visible(TemporalCatalog catalog, Scope scope, ActivitySession session);
	}
}
