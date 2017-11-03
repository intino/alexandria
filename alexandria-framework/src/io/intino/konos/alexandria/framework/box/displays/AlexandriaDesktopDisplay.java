package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.framework.box.displays.notifiers.AlexandriaDesktopDisplayNotifier;
import io.intino.konos.alexandria.framework.box.model.Desktop;
import io.intino.konos.alexandria.framework.box.model.Desktop.Layout;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AlexandriaDesktopDisplay extends AlexandriaElementDisplay<Desktop, AlexandriaDesktopDisplayNotifier> {
	private Map<Layout, Function<Layout, AlexandriaLayoutDisplay>> builders = new HashMap<>();

	public AlexandriaDesktopDisplay(Box box) {
		super(box);
		registerBuilders();
	}

	@Override
	public void reset() {
	}

	@Override
	protected void init() {
		super.init();
		Layout layout = element().layout();
		add(builders.get(layout).apply(layout));
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

	private void registerBuilders() {
		builders.put(Layout.Menu, layout -> new AlexandriaMenuLayoutDisplay(box));
		builders.put(Layout.Tab, layout -> new AlexandriaTabLayoutDisplay(box));
	}
}