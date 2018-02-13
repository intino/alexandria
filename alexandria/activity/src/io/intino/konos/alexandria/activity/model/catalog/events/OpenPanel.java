package io.intino.konos.alexandria.activity.model.catalog.events;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Panel;
import io.intino.konos.alexandria.activity.model.mold.stamps.Tree;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class OpenPanel extends Open {
	private Panel panel = null;
	private Breadcrumbs breadcrumbs = null;

	public Panel panel() {
		return panel;
	}

	public OpenPanel panel(Panel panel) {
		this.panel = panel;
		return this;
	}

	public Tree breadcrumbs(Item item, ActivitySession session) {
		return breadcrumbs != null ? breadcrumbs.tree(item.object(), session) : null;
	}

	public OpenPanel breadcrumbs(Breadcrumbs breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
		return this;
	}

	public interface Breadcrumbs {
		Tree tree(Object item, ActivitySession session);
	}
}