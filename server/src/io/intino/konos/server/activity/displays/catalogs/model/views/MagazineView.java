package io.intino.konos.server.activity.displays.catalogs.model.views;

public class MagazineView extends MoldView {
	private String noRecordMessage;
	private int width;

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
