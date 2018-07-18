package io.intino.konos.datalake.sensors;

import io.intino.konos.datalake.Sensor;
import io.intino.ness.inl.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class DeviceSensor extends Sensor {

	private List<Consumer<Message>> listeners = new ArrayList<>();

	public void addListener(Consumer<Message> consumer) {
		listeners.add(consumer);
	}


	protected void notifyEventReceived() {
		listeners.forEach(l -> l.accept(get()));
	}

}
