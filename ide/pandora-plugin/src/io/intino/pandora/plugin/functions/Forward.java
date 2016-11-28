package io.intino.pandora.plugin.functions;


import java.util.List;

@FunctionalInterface
public interface Forward {

	//	List<Message> forward();
	List<String> forward();
}
