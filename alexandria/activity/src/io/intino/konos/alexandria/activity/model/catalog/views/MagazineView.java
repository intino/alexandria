package io.intino.konos.alexandria.activity.model.catalog.views;

public class MagazineView extends MoldView {
	private String noRecordMessage;
	private int width = 100;

	public String noRecordMessage() {
		return noRecordMessage;
	}

	public MagazineView noRecordMessage(String noRecordMessage) {
		this.noRecordMessage = noRecordMessage;
		return this;
	}

	public int width() {
		return width;
	}

	public MagazineView width(int width) {
		this.width = width;
		return this;
	}
}
