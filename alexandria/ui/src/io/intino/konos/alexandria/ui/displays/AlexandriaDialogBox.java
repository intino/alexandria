package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.DisplayReferenceBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaDialogBoxNotifier;
import io.intino.konos.alexandria.ui.schemas.DialogBoxSettings;
import io.intino.konos.alexandria.ui.schemas.Position;
import io.intino.konos.alexandria.ui.schemas.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlexandriaDialogBox extends ActivityDisplay<AlexandriaDialogBoxNotifier, Box> {
	private String label;
	private AlexandriaDisplay display;
	private Class<? extends AlexandriaDisplay> displayType;
	private DialogBoxSettings settings;
	private List<Consumer<Boolean>> acceptListeners = new ArrayList<>();

	public AlexandriaDialogBox(Box box) {
		super(box);
	}

	public void label(String label) {
		this.label = label;
	}

	public void display(AlexandriaDisplay display) {
		acceptListeners.clear();
		this.display = display;
		this.displayType = display.getClass();
	}

	@Override
	public void refresh() {
		super.refresh();
		if (displayType != null) remove(displayType);
		if (display == null) return;
		if (settings == null) return;
		sendInfo();
		add(display);
		display.personifyOnce(id());
	}

	public void settings(int width, int height) {
		settings(width, height, false);
	}

	public void settings(int width, int height, boolean acceptButton) {
		settings(width, height, acceptButton, null);
	}

	public void settings(int width, int height, boolean acceptButton, Position position) {
		this.settings = new DialogBoxSettings().position(position).acceptButton(acceptButton).size(new Size().width(width).height(height));
	}

	public void close() {
		notifier.closeDialog();
	}

	private void sendInfo() {
		notifier.refreshDisplay(DisplayReferenceBuilder.build(label(), displayType.getSimpleName()));
		notifier.refreshSettings(settings);
	}

	private String label() {
		return label;
	}

	public void accept() {
		acceptListeners.forEach(l -> l.accept(false));
	}

	public void onAccept(Consumer<Boolean> listener) {
		acceptListeners.add(listener);
	}

	public interface LabelProvider {
		String label(AlexandriaDisplay display);
	}
}