package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.framework.box.displays.notifiers.AlexandriaDesktopDisplayNotifier;
import io.intino.konos.alexandria.framework.box.model.Desktop;

public class AlexandriaDesktopDisplay<DN extends AlexandriaDesktopDisplayNotifier> extends AlexandriaElementDisplay<Desktop, DN> {

	public AlexandriaDesktopDisplay(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	@Override
	protected void init() {
		super.init();
		AlexandriaLayoutDisplay display = element().layoutDisplay();
		display.onLoading((value) -> AlexandriaDesktopDisplay.this.refreshLoading((Boolean) value));
		display.onLoaded((value) -> AlexandriaDesktopDisplay.this.refreshLoaded());
		notifier.displayType(display.name());
		addAndPersonify(display);
	}

	@Override
	protected void showDialog() {
	}

	@Override
	protected void currentItem(String id) {
	}

	@Override
	protected io.intino.konos.alexandria.framework.box.model.Item currentItem() {
		return null;
	}

	@Override
	protected void notifyFiltered(boolean value) {
	}

	@Override
	protected void refreshBreadcrumbs(String breadcrumbs) {
	}

	@Override
	protected void createPanel(String item) {
	}

	@Override
	protected void showPanel() {
	}

	@Override
	protected void hidePanel() {
	}

	private void add(AlexandriaLayoutDisplay display) {
		addListeners(display);
		addAndPersonify(display);
	}

	private void addListeners(AlexandriaLayoutDisplay display) {
		display.onLoading(withMessage -> refreshLoading((Boolean) withMessage));
		display.onLoaded(value -> refreshLoaded());
	}

	private void refreshLoading(boolean withMessage) {
		notifier.loading(withMessage);
	}

	private void refreshLoaded() {
		notifier.loaded();
	}

}