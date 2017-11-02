package io.intino.konos.alexandria.framework.box.model.catalog.views;

public class GridView extends MoldView {
	private String noRecordsMessage;
	private int width;

	public String noRecordsMessage() {
		return noRecordsMessage;
	}

	public GridView noRecordsMessage(String noRecordsMessage) {
		this.noRecordsMessage = noRecordsMessage;
		return this;
	}

	public int width() {
		return width;
	}

	public GridView width(int width) {
		this.width = width;
		return this;
	}
}
