package io.intino.alexandria.core;

import io.intino.ness.inl.Message;

public abstract class Sensor {
	public abstract Message get(Object... args);
}
