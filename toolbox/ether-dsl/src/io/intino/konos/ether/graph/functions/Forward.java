package io.intino.konos.ether.graph.functions;


import java.util.List;

@FunctionalInterface
public interface Forward {

	//	List<Message> forward();
	List<String> forward();
}
