package io.intino.konos.framework.sensors;

public abstract class DocumentEditionSensor extends UserSensor {
	private final Mode mode;

	public enum Mode { Offline, Online };

	public DocumentEditionSensor(String mode) {
		this.mode = Mode.valueOf(mode);

	}

	public Mode mode() {
		return mode;
	}
}
