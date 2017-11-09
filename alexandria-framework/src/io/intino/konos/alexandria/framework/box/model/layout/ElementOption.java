package io.intino.konos.alexandria.framework.box.model.layout;

import io.intino.konos.alexandria.framework.box.model.layout.options.Group;

public class ElementOption {
	private Group owner;

	public Group owner() {
		return owner;
	}

	public ElementOption owner(Group owner) {
		this.owner = owner;
		return this;
	}
}
