package io.intino.alexandria.ui.model.datasource.placemarks;

import io.intino.alexandria.ui.model.datasource.PlaceMark;

public class Point<O> extends PlaceMark<O> {
	private long latitude;
	private long longitude;

	public long latitude() {
		return latitude;
	}

	public Point latitude(long latitude) {
		this.latitude = latitude;
		return this;
	}

	public long longitude() {
		return longitude;
	}

	public Point longitude(long longitude) {
		this.longitude = longitude;
		return this;
	}
}
