package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.locations.Point;

public class BoundingBox {
	private Point northEast;
	private Point southWest;

	public Point northEast() {
		return northEast;
	}

	public BoundingBox northEast(Point northEast) {
		this.northEast = northEast;
		return this;
	}

	public Point southWest() {
		return southWest;
	}

	public BoundingBox southWest(Point southWest) {
		this.southWest = southWest;
		return this;
	}
}
