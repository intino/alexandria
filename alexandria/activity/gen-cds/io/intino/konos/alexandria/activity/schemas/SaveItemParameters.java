package io.intino.konos.alexandria.activity.schemas;

public class SaveItemParameters implements java.io.Serializable {

	private String stamp = "";
	private String item = "";
	private String value = "";

	public String stamp() {
		return this.stamp;
	}

	public String item() {
		return this.item;
	}

	public String value() {
		return this.value;
	}

	public SaveItemParameters stamp(String stamp) {
		this.stamp = stamp;
		return this;
	}

	public SaveItemParameters item(String item) {
		this.item = item;
		return this;
	}

	public SaveItemParameters value(String value) {
		this.value = value;
		return this;
	}
}