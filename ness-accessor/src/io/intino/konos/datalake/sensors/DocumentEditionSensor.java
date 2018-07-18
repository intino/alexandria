package io.intino.konos.datalake.sensors;

import io.intino.konos.datalake.Sensor;

public abstract class DocumentEditionSensor extends Sensor {
	private final Mode mode;

	public enum Mode { Offline, Online };

	public DocumentEditionSensor(String mode) {
		this.mode = Mode.valueOf(mode);

	}

	public Mode mode() {
		return mode;
	}
}
