package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class Map extends Stamp<String> {
	private int zoom;
	private double latitude;
	private double longitude;

	@Override
	public String objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

	public int zoom() {
		return zoom;
	}

	public Map zoom(int zoom) {
		this.zoom = zoom;
		return this;
	}

	public double latitude() {
		return latitude;
	}

	public Map latitude(double latitude) {
		this.latitude = latitude;
		return this;
	}

	public double longitude() {
		return longitude;
	}

	public Map longitude(double longitude) {
		this.longitude = longitude;
		return this;
	}
}
