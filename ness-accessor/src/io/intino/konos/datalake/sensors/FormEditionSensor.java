package io.intino.konos.datalake.sensors;

public abstract class FormEditionSensor extends UserSensor {
	private final String path;

	public FormEditionSensor(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}
}
