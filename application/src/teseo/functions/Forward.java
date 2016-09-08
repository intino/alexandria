package teseo.functions;

import teseo.Message;

import java.util.List;

@FunctionalInterface
public interface Forward {

	List<Message> forward();
}
