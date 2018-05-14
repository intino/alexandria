package io.intino.konos.alexandria.ui.model.view.container;

import io.intino.konos.alexandria.ui.model.Mold;

public abstract class CollectionContainer extends Container {
	private String noRecordsMessage;
	private Mold mold;

	public String noRecordsMessage() {
		return noRecordsMessage;
	}

	public CollectionContainer noRecordsMessage(String noRecordsMessage) {
		this.noRecordsMessage = noRecordsMessage;
		return this;
	}

	public Mold mold() {
		return mold;
	}

	public CollectionContainer mold(Mold mold) {
		this.mold = mold;
		return this;
	}

}
