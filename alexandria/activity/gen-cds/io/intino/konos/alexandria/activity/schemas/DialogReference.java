package io.intino.konos.alexandria.activity.schemas;

public class DialogReference implements java.io.Serializable {

	private String location = "";
	private Integer width = 0;
	private Integer height = 0;

	public String location() {
		return this.location;
	}

	public Integer width() {
		return this.width;
	}

	public Integer height() {
		return this.height;
	}

	public DialogReference location(String location) {
		this.location = location;
		return this;
	}

	public DialogReference width(Integer width) {
		this.width = width;
		return this;
	}

	public DialogReference height(Integer height) {
		this.height = height;
		return this;
	}
}