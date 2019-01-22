package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.ui.displays.notifiers.AlexandriaTextNotifier;


public class AlexandriaText extends AlexandriaValue<AlexandriaTextNotifier, String> {

    @Override
    protected void notifyValue(String value) {
        notifier.update(value);
    }

}