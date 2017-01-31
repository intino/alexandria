package io.intino.konos.server.activity.displays;

public interface DisplayNotifierProvider {
    DisplayNotifier agent(Display display, MessageCarrier carrier);
}
