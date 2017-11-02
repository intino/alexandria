package io.intino.alexandria.foundation.activity.displays;

public interface DisplayNotifierProvider {
    DisplayNotifier agent(Display display, MessageCarrier carrier);
}
