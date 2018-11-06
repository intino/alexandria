package io.intino.konos.framework;

import io.intino.ness.inl.Message;

public abstract class Sensor {
	public abstract Message get(Object... args);
}
