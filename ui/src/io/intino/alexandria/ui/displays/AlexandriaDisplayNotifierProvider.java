package io.intino.alexandria.ui.displays;

import io.intino.alexandria.rest.pushservice.MessageCarrier;

public interface AlexandriaDisplayNotifierProvider {
    AlexandriaDisplayNotifier agent(AlexandriaDisplay display, MessageCarrier carrier);
}
