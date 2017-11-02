package io.intino.alexandria.framework.box.model.catalog.events;

import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.mold.stamps.Tree;
import io.intino.alexandria.framework.box.model.Panel;

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

	public Tree breadcrumbs(Item item, String username) {
		return breadcrumbs != null ? breadcrumbs.tree(item.object(), username) : null;
	}

	public OpenPanel breadcrumbs(Breadcrumbs breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
		return this;
	}

	public interface Breadcrumbs {
		Tree tree(Object item, String username);
	}
}
