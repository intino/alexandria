package io.intino.konos.alexandria.activity.box.displays;

public interface AlexandriaDisplayNotifierProvider {
    AlexandriaDisplayNotifier agent(AlexandriaDisplay display, MessageCarrier carrier);
}
