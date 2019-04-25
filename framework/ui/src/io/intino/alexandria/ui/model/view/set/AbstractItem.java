package io.intino.alexandria.ui.model.view.set;

import io.intino.alexandria.ui.model.view.set.item.Group;
import io.intino.alexandria.ui.services.push.UISession;

public class AbstractItem {
	private String name;
	private Group owner;
	private Hidden hidden = null;

	public String name() {
		return name;
	}

	public AbstractItem name(String name) {
		this.name = name;
		return this;
	}

	public Group owner() {
		return owner;
	}

	public AbstractItem owner(Group owner) {
		this.owner = owner;
		return this;
	}

	public boolean hidden(UISession session) {
		return hidden != null && hidden.hidden(session);
	}

	public AbstractItem hidden(Hidden hidden) {
		this.hidden = hidden;
		return this;
	}

	public interface Hidden {
		boolean hidden(UISession session);
	}

}
