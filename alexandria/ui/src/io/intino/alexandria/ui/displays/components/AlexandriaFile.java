package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.ui.displays.notifiers.AlexandriaFileNotifier;

import java.net.URL;

public class AlexandriaFile extends AlexandriaValue<AlexandriaFileNotifier, URL> {

    @Override
    protected void notifyValue(URL value) {
        notifier.update(assetUrl(value));
    }

}