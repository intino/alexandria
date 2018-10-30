package io.intino.alexandria.nessaccesor.sensors;

import io.intino.alexandria.nessaccesor.Sensor;

public abstract class UserSensor extends Sensor {
	private int width = 100;
	private int height = 100;

	public int width() {
		return width;
	}

	public UserSensor width(int width) {
		this.width = width;
		return this;
	}

	public int height() {
		return height;
	}

	public UserSensor height(int height) {
		this.height = height;
		return this;
	}
}
