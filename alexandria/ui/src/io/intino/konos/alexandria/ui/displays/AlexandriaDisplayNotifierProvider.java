package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.rest.pushservice.MessageCarrier;

public interface AlexandriaDisplayNotifierProvider {
    AlexandriaDisplayNotifier agent(AlexandriaDisplay display, MessageCarrier carrier);
}
