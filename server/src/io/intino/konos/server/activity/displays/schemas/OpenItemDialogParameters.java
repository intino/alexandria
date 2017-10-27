package io.intino.konos.server.activity.displays.schemas;

public class OpenItemDialogParameters implements java.io.Serializable {

	private String item = "";
	private String stamp = "";

	public String item() {
		return this.item;
	}

	public String stamp() {
		return this.stamp;
	}

	public OpenItemDialogParameters item(String item) {
		this.item = item;
		return this;
	}

	public OpenItemDialogParameters stamp(String stamp) {
		this.stamp = stamp;
		return this;
	}
}