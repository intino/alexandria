package io.intino.alexandria.restful;

import java.util.function.Consumer;

public interface RestfulNotifier {
    void listen(Consumer<String> listener, String webSocketUri);
}
