package io.intino.konos.datalake.sensors;

import io.intino.konos.datalake.Sensor;

public abstract class FormEditionSensor extends Sensor {
	private final String path;

	public FormEditionSensor(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}
}
