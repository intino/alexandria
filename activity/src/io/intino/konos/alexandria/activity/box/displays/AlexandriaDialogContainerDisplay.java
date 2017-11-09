package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.box.displays.ActivityDisplay;
import io.intino.konos.alexandria.activity.box.displays.builders.DialogReferenceBuilder;
import io.intino.konos.alexandria.activity.box.displays.notifiers.AlexandriaDialogContainerDisplayNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlexandriaDialogContainerDisplay extends ActivityDisplay<AlexandriaDialogContainerDisplayNotifier> {
	private int width;
	private int height;
	private String location;
	private List<Consumer<String>> assertionListeners = new ArrayList<>();

	public AlexandriaDialogContainerDisplay(Box box) {
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