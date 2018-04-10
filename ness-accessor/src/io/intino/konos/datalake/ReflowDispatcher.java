package io.intino.konos.datalake;

import io.intino.ness.inl.Message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflowDispatcher {


	private Map<String, MessageHandler> handlers;

	public ReflowDispatcher(List<Datalake.Tank> tanks, MessageHandler onBlock, MessageHandler onFinish) {
		this.handlers = tanks.stream().collect(Collectors.toMap(Datalake.Tank::name, t -> t::handle));
		handlers.put("endblock", onBlock);
		handlers.put("endreflow", onFinish);
	}

	public void dispatch(Message message) {
		handlers.get(message.type()).handle(message);
	}

}
