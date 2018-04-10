package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.Datalake.Tank;
import io.intino.konos.datalake.Ness;
import io.intino.konos.jms.TopicConsumer;
import io.intino.ness.inl.Message;

public class FSTank implements Tank {

	private final String name;
	private final FSDatalake datalake;

	public FSTank(String name, FSDatalake datalake) {
		this.name = name;
		this.datalake = datalake;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public void feed(Message message) {
		datalake.drop(name, message);
	}

	@Override
	public void drop(Message message) {
		datalake.drop(name, message);
	}

	@Override
	public TopicConsumer flow(Ness.TankFlow flow) {
		return null;
	}

	@Override
	public TopicConsumer flow(Ness.TankFlow flow, String flowID) {
		return null;
	}

	@Override
	public void unregister() {

	}
}
