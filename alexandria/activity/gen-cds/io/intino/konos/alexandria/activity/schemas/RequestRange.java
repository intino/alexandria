package io.intino.konos.alexandria.activity.schemas;

public class RequestRange implements java.io.Serializable {

	private java.time.Instant from;
	private java.time.Instant to;

	public java.time.Instant from() {
		return this.from;
	}

	public java.time.Instant to() {
		return this.to;
	}

	public RequestRange from(java.time.Instant from) {
		this.from = from;
		return this;
	}

	public RequestRange to(java.time.Instant to) {
		this.to = to;
		return this;
	}
}