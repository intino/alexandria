package io.intino.alexandria.ui.displays.components.slider;

public class Animation {
	public int interval;
	public boolean loop;

	public int interval() {
		return interval;
	}

	public Animation interval(int interval) {
		this.interval = interval;
		return this;
	}

	public boolean isloop() {
		return loop;
	}

	public Animation loop(boolean loop) {
		this.loop = loop;
		return this;
	}
}
