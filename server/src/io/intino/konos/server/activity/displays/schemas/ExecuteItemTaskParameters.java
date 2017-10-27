package io.intino.konos.server.activity.displays.schemas;

public class ExecuteItemTaskParameters implements java.io.Serializable {

	private String item = "";
	private String stamp = "";

	public String item() {
		return this.item;
	}

	public String stamp() {
		return this.stamp;
	}

	public ExecuteItemTaskParameters item(String item) {
		this.item = item;
		return this;
	}

	public ExecuteItemTaskParameters stamp(String stamp) {
		this.stamp = stamp;
		return this;
	}
}