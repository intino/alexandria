package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.ActivityDisplay;
import io.intino.konos.server.activity.displays.elements.builders.DialogReferenceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DialogContainerDisplay extends ActivityDisplay<DialogContainerDisplayNotifier> {
    private int width;
    private int height;
    private String location;
    private List<Consumer<String>> assertionListeners = new ArrayList<>();

    public DialogContainerDisplay(Box box) {
        super(box);
    }

    public void dialogWidth(int width) {
        this.width = width;
    }

    public void dialogHeight(int height) {
        this.height = height;
    }

    public void dialogLocation(String location) {
        this.location = location;
    }

    public void onDialogAssertion(Consumer<String> listener) {
        this.assertionListeners.add(listener);
    }

    @Override
    public void refresh() {
        super.refresh();
        sendInfo();
    }

    private void sendInfo() {
        notifier.refreshDialog(DialogReferenceBuilder.build(location, width, height));
    }

    public void dialogAssertionMade(String modification) {
        assertionListeners.forEach(l -> l.accept(modification));
    }

}