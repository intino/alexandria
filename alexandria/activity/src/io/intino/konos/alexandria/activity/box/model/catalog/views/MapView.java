package io.intino.konos.alexandria.activity.box.model.catalog.views;

public class MapView extends MoldView {
	private Center center;
	private Zoom zoom;

	public Center center() {
		return center;
	}

	public MapView center(Center center) {
		this.center = center;
		return this;
	}

	public Zoom zoom() {
		return zoom;
	}

	public MapView zoom(Zoom zoom) {
		this.zoom = zoom;
		return this;
	}

	public static class Center {
		private double latitude;
		private double longitude;

		public double latitude() {
			return latitude;
		}

		public Center latitude(double latitude) {
			this.latitude = latitude;
			return this;
		}

		public double longitude() {
			return longitude;
		}

		public Center longitude(double longitude) {
			this.longitude = longitude;
			return this;
		}
	}

	public static class Zoom {
		private int defaultZoom;
		private int min;
		private int max;

		public int defaultZoom() {
			return defaultZoom;
		}

		public Zoom defaultZoom(int value) {
			this.defaultZoom = value;
			return this;
		}

		public int min() {
			return min;
		}

		public Zoom min(int min) {
			this.min = min;
			return this;
		}

		public int max() {
			return max;
		}

		public Zoom max(int max) {
			this.max = max;
			return this;
		}
	}
}
