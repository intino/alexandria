package io.intino.konos.server.activity.displays.schemas;

public class ExportItemParameters implements java.io.Serializable {
	private String stamp = "";
	private String option = "";
	private java.time.Instant from;
	private java.time.Instant to;

	public String stamp() {
		return this.stamp;
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

	public ExportItemParameters stamp(String stamp) {
		this.stamp = stamp;
		return this;
	}

	public ExportItemParameters option(String option) {
		this.option = option;
		return this;
	}

	public ExportItemParameters from(java.time.Instant from) {
		this.from = from;
		return this;
	}

	public ExportItemParameters to(java.time.Instant to) {
		this.to = to;
		return this;
	}
}