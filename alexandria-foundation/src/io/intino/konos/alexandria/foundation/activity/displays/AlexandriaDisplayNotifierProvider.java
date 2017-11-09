package io.intino.konos.alexandria.foundation.activity.displays;

public interface AlexandriaDisplayNotifierProvider {
    AlexandriaDisplayNotifier agent(AlexandriaDisplay display, MessageCarrier carrier);
}
