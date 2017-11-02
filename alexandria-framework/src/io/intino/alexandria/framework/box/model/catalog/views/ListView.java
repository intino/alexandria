package io.intino.alexandria.framework.box.model.catalog.views;

public class ListView extends MoldView {
	private String noRecordsMessage;
	private int width;

	public String noRecordsMessage() {
		return noRecordsMessage;
	}

	public ListView noRecordsMessage(String noRecordsMessage) {
		this.noRecordsMessage = noRecordsMessage;
		return this;
	}

	public int width() {
		return width;
	}

	public ListView width(int width) {
		this.width = width;
		return this;
	}
}
