package io.intino.konos.alexandria.ui.model.mold.stamps;

public class ChartData {
	private String title;

	public String title() {
		return title;
	}

	public <T extends ChartData> T title(String title) {
		this.title = title;
		return (T) this;
	}
}