package io.intino.konos.alexandria.activity.schemas;

public class PictureData implements java.io.Serializable {

	private String item = "";
	private String stamp = "";
	private String value = "";

	public String item() {
		return this.item;
	}

	public String stamp() {
		return this.stamp;
	}

	public String value() {
		return this.value;
	}

	public PictureData item(String item) {
		this.item = item;
		return this;
	}

	public PictureData stamp(String stamp) {
		this.stamp = stamp;
		return this;
	}

	public PictureData value(String value) {
		this.value = value;
		return this;
	}
}