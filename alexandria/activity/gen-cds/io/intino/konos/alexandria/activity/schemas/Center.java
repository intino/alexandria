package io.intino.konos.alexandria.activity.schemas;

public class Center implements java.io.Serializable {

	private double latitude = 0.0;
	private double longitude = 0.0;

	public double latitude() {
		return this.latitude;
	}

	public double longitude() {
		return this.longitude;
	}

	public Center latitude(double latitude) {
		this.latitude = latitude;
		return this;
	}

	public Center longitude(double longitude) {
		this.longitude = longitude;
		return this;
	}
}