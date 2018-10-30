package io.intino.alexandria.nessaccesor;

import io.intino.ness.inl.Message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflowDispatcher {
	private final List<NessAccessor.EventStore.Tank> tanks;
	private Map<String, MessageHandler> handlers;

	public ReflowDispatcher(List<NessAccessor.EventStore.Tank> tanks, MessageHandler onBlock, MessageHandler onFinish) {
		this.tanks = tanks;
		this.handlers = tanks.stream().collect(Collectors.toMap(this::typeOf, t -> t::handle));
		handlers.put("endblock", onBlock);
		handlers.put("endreflow", onFinish);
	}

	private String typeOf(NessAccessor.EventStore.Tank tank) {
		final String name = tank.name().toLowerCase();
		return name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : name;
	}

	public void dispatch(Message message) {
		handlers.get(message.type().toLowerCase()).handle(message);
	}

	public List<NessAccessor.EventStore.Tank> tanks() {
		return tanks;
	}
}
