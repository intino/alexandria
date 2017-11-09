package io.intino.konos.alexandria.activity.schemas;

public class Bounds implements java.io.Serializable {

	private NorthEast northEast;
	private SouthWest southWest;

	public NorthEast northEast() {
		return this.northEast;
	}

	public SouthWest southWest() {
		return this.southWest;
	}

	public Bounds northEast(NorthEast northEast) {
		this.northEast = northEast;
		return this;
	}

	public Bounds southWest(SouthWest southWest) {
		this.southWest = southWest;
		return this;
	}
}