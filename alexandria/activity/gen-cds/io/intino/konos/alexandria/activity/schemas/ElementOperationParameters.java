package io.intino.konos.alexandria.activity.schemas;

public class ElementOperationParameters implements java.io.Serializable {

	private String operation = "";
	private String option = "";
	private java.time.Instant from;
	private java.time.Instant to;

	public String operation() {
		return this.operation;
	}

	public String option() {
		return this.option;
	}

	public java.time.Instant from() {
		return this.from;
	}

	public java.time.Instant to() {
		return this.to;
	}

	public ElementOperationParameters operation(String operation) {
		this.operation = operation;
		return this;
	}

	public ElementOperationParameters option(String option) {
		this.option = option;
		return this;
	}

	public ElementOperationParameters from(java.time.Instant from) {
		this.from = from;
		return this;
	}

	public ElementOperationParameters to(java.time.Instant to) {
		this.to = to;
		return this;
	}
}