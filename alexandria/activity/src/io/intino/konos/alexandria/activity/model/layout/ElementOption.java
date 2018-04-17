package io.intino.konos.alexandria.activity.model.layout;

import io.intino.konos.alexandria.activity.model.layout.options.Group;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class ElementOption {
	private Group owner;
	private Hidden hidden = null;

	public Group owner() {
		return owner;
	}

	public ElementOption owner(Group owner) {
		this.owner = owner;
		return this;
	}

	public boolean hidden(ActivitySession session) {
		return hidden != null && hidden.hidden(session);
	}

	public ElementOption hidden(Hidden hidden) {
		this.hidden = hidden;
		return this;
	}

	public interface Hidden {
		boolean hidden(ActivitySession session);
	}

}
