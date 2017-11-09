package io.intino.konos.alexandria.activity.box.model.catalog;

import io.intino.konos.alexandria.activity.box.model.catalog.events.OnClickRecord;

public class Events {
	private OnClickRecord onClickRecord;

	public OnClickRecord onClickRecord() {
		return onClickRecord;
	}

	public Events onClickRecord(OnClickRecord onClickRecord) {
		this.onClickRecord = onClickRecord;
		return this;
	}
}
