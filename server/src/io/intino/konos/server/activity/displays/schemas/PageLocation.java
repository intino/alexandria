package io.intino.konos.server.activity.displays.schemas;

public class PageLocation implements java.io.Serializable {

	private String value = "";
	private Boolean internal = true;

	public String value() {
		return this.value;
	}

	public Boolean internal() {
		return this.internal;
	}

	public PageLocation value(String value) {
		this.value = value;
		return this;
	}

	public PageLocation internal(Boolean internal) {
		this.internal = internal;
		return this;
	}
}