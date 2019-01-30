package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.rest.pushservice.MessageCarrier;
import io.intino.alexandria.ui.displays.AlexandriaDisplay;

public interface AlexandriaDisplayNotifierProvider {
    DisplayNotifier notifier(AlexandriaDisplay display, MessageCarrier carrier);
}
