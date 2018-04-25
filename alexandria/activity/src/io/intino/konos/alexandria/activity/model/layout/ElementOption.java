package io.intino.konos.alexandria.activity.model.layout;

import io.intino.konos.alexandria.activity.model.layout.options.Group;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class ElementOption {
	private String name;
	private Group owner;
	private Hidden hidden = null;

	public String name() {
		return name;
	}

	public ElementOption name(String name) {
		this.name = name;
		return this;
	}

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
