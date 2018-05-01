package io.intino.konos.alexandria.ui.model.views;

import io.intino.konos.alexandria.ui.model.Mold;
import io.intino.konos.alexandria.ui.model.View;

public abstract class CatalogView extends View {
	private String noRecordsMessage;
	private Mold mold;

	public String noRecordsMessage() {
		return noRecordsMessage;
	}

	public CatalogView noRecordsMessage(String noRecordsMessage) {
		this.noRecordsMessage = noRecordsMessage;
		return this;
	}

	public Mold mold() {
		return mold;
	}

	public CatalogView mold(Mold mold) {
		this.mold = mold;
		return this;
	}

}
