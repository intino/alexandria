package io.intino.konos.datalake;

import io.intino.ness.inl.Message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflowDispatcher {
	private final List<EventDatalake.Tank> tanks;
	private Map<String, MessageHandler> handlers;

	public ReflowDispatcher(List<EventDatalake.Tank> tanks, MessageHandler onBlock, MessageHandler onFinish) {
		this.tanks = tanks;
		this.handlers = tanks.stream().collect(Collectors.toMap(this::typeOf, t -> t::handle));
		handlers.put("endblock", onBlock);
		handlers.put("endreflow", onFinish);
	}

	private String typeOf(EventDatalake.Tank tank) {
		final String name = tank.name().toLowerCase();
		return name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : name;
	}

	public void dispatch(Message message) {
		handlers.get(message.type().toLowerCase()).handle(message);
	}

	public List<EventDatalake.Tank> tanks() {
		return tanks;
	}
}
