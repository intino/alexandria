package io.intino.konos.alexandria.activity.schemas;

public class DownloadItemParameters implements java.io.Serializable {

	private String stamp = "";
	private String option = "";

	public String stamp() {
		return this.stamp;
	}

	public String option() {
		return this.option;
	}

	public DownloadItemParameters stamp(String stamp) {
		this.stamp = stamp;
		return this;
	}

	public DownloadItemParameters option(String option) {
		this.option = option;
		return this;
	}
}