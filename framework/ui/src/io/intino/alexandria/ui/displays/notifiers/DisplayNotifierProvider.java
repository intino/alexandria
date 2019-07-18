package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.rest.pushservice.MessageCarrier;
import io.intino.alexandria.ui.displays.Display;

public interface DisplayNotifierProvider {
    DisplayNotifier notifier(Display display, MessageCarrier carrier);
}
