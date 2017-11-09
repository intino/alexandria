package io.intino.konos.alexandria.activity.schemas;

public class SouthWest implements java.io.Serializable {

	private double longitude = 0.0;
	private double latitude = 0.0;

	public double longitude() {
		return this.longitude;
	}

	public double latitude() {
		return this.latitude;
	}

	public SouthWest longitude(double longitude) {
		this.longitude = longitude;
		return this;
	}

	public SouthWest latitude(double latitude) {
		this.latitude = latitude;
		return this;
	}
}