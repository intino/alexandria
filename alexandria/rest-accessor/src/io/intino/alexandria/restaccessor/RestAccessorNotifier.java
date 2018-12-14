package io.intino.alexandria.restaccessor;

import java.util.function.Consumer;

public interface RestAccessorNotifier {
	void listen(Consumer<String> listener, String webSocketUri);

	void close();
}
