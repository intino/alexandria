package io.intino.konos.server.activity.displays.schemas;

public class Group implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private Boolean selected = true;
	private Integer count = 0;

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public Boolean selected() {
		return this.selected;
	}

	public Integer count() {
		return this.count;
	}

	public Group name(String name) {
		this.name = name;
		return this;
	}

	public Group label(String label) {
		this.label = label;
		return this;
	}

	public Group selected(Boolean selected) {
		this.selected = selected;
		return this;
	}

	public Group count(Integer count) {
		this.count = count;
		return this;
	}
}