package io.intino.konos.alexandria.activity.model.catalog.events;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Panel;
import io.intino.konos.alexandria.activity.model.mold.stamps.Tree;
import io.intino.konos.alexandria.activity.services.push.User;

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

	public Tree breadcrumbs(Item item, User user) {
		return breadcrumbs != null ? breadcrumbs.tree(item.object(), user) : null;
	}

	public OpenPanel breadcrumbs(Breadcrumbs breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
		return this;
	}

	public interface Breadcrumbs {
		Tree tree(Object item, User user);
	}
}
