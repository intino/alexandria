package io.intino.konos.alexandria.foundation.activity.displays;

public interface DisplayNotifierProvider {
    DisplayNotifier agent(Display display, MessageCarrier carrier);
}
