package io.intino.alexandria.ui.services.push;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UIMessage implements Serializable {
	@SerializedName("op") private String operation;
	@SerializedName("s") private String sender;
	@SerializedName("d") private String display;
	@SerializedName("v") private String value;

	public String operation() {
		return operation;
	}

	public UIMessage operation(String operation) {
		this.operation = operation;
		return this;
	}

	public String sender() {
		return sender;
	}

	public UIMessage sender(String sender) {
		this.sender = sender;
		return this;
	}

	public String display() {
		return display;
	}

	public UIMessage display(String display) {
		this.display = display;
		return this;
	}

	public String value() {
		return value;
	}

	public UIMessage value(String value) {
		this.value = value;
		return this;
	}

}
