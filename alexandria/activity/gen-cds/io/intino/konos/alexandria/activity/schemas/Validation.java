package io.intino.konos.alexandria.activity.schemas;

public class Validation implements java.io.Serializable {

	private String input = "";
	private Boolean status = true;
	private String message = "";
	private String modifiedInputs = "";

	public String input() {
		return this.input;
	}

	public Boolean status() {
		return this.status;
	}

	public String message() {
		return this.message;
	}

	public String modifiedInputs() {
		return this.modifiedInputs;
	}

	public Validation input(String input) {
		this.input = input;
		return this;
	}

	public Validation status(Boolean status) {
		this.status = status;
		return this;
	}

	public Validation message(String message) {
		this.message = message;
		return this;
	}

	public Validation modifiedInputs(String modifiedInputs) {
		this.modifiedInputs = modifiedInputs;
		return this;
	}
}