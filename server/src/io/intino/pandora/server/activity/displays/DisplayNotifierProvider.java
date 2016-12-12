package io.intino.pandora.server.activity.displays;

public interface DisplayNotifierProvider {
    DisplayNotifier agent(Display display, MessageCarrier carrier);
}
