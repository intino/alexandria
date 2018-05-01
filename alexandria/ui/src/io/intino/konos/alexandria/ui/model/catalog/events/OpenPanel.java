package io.intino.konos.alexandria.ui.model.catalog.events;

import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Panel;
import io.intino.konos.alexandria.ui.model.mold.stamps.Tree;
import io.intino.konos.alexandria.ui.services.push.UISession;

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

	public Tree breadcrumbs(Item item, UISession session) {
		return breadcrumbs != null ? breadcrumbs.tree(item.object(), session) : null;
	}

	public OpenPanel breadcrumbs(Breadcrumbs breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
		return this;
	}

	public interface Breadcrumbs {
		Tree tree(Object item, UISession session);
	}
}