package io.intino.konos.alexandria.activity.schemas;

public class Zoom implements java.io.Serializable {

	private Integer min = 0;
	private Integer max = 0;
	private Integer defaultValue = 0;

	public Integer min() {
		return this.min;
	}

	public Integer max() {
		return this.max;
	}

	public Integer defaultValue() {
		return this.defaultValue;
	}

	public Zoom min(Integer min) {
		this.min = min;
		return this;
	}

	public Zoom max(Integer max) {
		this.max = max;
		return this;
	}

	public Zoom defaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
}