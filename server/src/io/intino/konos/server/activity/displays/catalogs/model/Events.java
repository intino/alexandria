package io.intino.konos.server.activity.displays.catalogs.model;

import io.intino.konos.server.activity.displays.catalogs.model.events.OnClickRecord;

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
