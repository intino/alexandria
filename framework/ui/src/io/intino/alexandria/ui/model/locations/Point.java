package io.intino.alexandria.ui.model.locations;

import io.intino.alexandria.ui.model.Geometry;

import java.util.Collections;
import java.util.List;

public class Point extends Geometry {
	private double latitude;
	private double longitude;

	public Point(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double latitude() {
		return latitude;
	}

	public Point latitude(double latitude) {
		this.latitude = latitude;
		return this;
	}

	public double longitude() {
		return longitude;
	}

	public Point longitude(double longitude) {
		this.longitude = longitude;
		return this;
	}

	@Override
	public String toWkt() {
		return String.format("POINT(%f %f)", latitude, longitude);
	}

}