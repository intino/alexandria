package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.services.push.UISession;

public class Map extends Stamp<String> {
	private int zoom;
	private double latitude;
	private double longitude;

	@Override
	public String objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
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
