package io.intino.alexandria.ui.model.datasource.placemarks;

import io.intino.alexandria.ui.model.datasource.PlaceMark;

public class Rectangle<O> extends PlaceMark<O> {
	private Point north;
	private Point south;
	private Point east;
	private Point west;

	public Point north() {
		return north;
	}

	public Rectangle north(Point north) {
		this.north = north;
		return this;
	}

	public Point south() {
		return south;
	}

	public Rectangle south(Point south) {
		this.south = south;
		return this;
	}

	public Point east() {
		return east;
	}

	public Rectangle east(Point east) {
		this.east = east;
		return this;
	}

	public Point west() {
		return west;
	}

	public Rectangle west(Point west) {
		this.west = west;
		return this;
	}
}
