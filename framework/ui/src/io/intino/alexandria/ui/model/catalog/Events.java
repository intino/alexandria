package io.intino.alexandria.ui.model.catalog;

import io.intino.alexandria.ui.model.catalog.events.OnClickItem;

public class Events {
	private OnClickItem onClickItem;

	public OnClickItem onClickItem() {
		return onClickItem;
	}

	public Events onClickItem(OnClickItem onClickItem) {
		this.onClickItem = onClickItem;
		return this;
	}
}
