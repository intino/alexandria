package io.intino.konos.alexandria.activity.displays;

public interface AlexandriaDisplayNotifierProvider {
    AlexandriaDisplayNotifier agent(AlexandriaDisplay display, MessageCarrier carrier);
}
