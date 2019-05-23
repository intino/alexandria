package io.intino.alexandria.ui.model.datasource;

public class PlaceMark<O> {
	private O item;

	public O item() {
		return item;
	}

	public PlaceMark item(O item) {
		this.item = item;
		return this;
	}
}
