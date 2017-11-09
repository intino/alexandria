package io.intino.konos.alexandria.activity.schemas;

public class ItemBlock implements java.io.Serializable {

	private String name = "";
	private Boolean hidden = true;

	public String name() {
		return this.name;
	}

	public Boolean hidden() {
		return this.hidden;
	}

	public ItemBlock name(String name) {
		this.name = name;
		return this;
	}

	public ItemBlock hidden(Boolean hidden) {
		this.hidden = hidden;
		return this;
	}
}