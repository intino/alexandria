package io.intino.konos.server.activity.displays.molds;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.elements.ElementDisplay;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.molds.model.Mold;

public abstract class MoldDisplay<DN extends MoldDisplayNotifier> extends ElementDisplay<Mold, DN> {

	public MoldDisplay(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	@Override
	protected void showDialog() {
	}

	@Override
	protected Item currentItem() {
		return null;
	}

	@Override
	protected void currentItem(String id) {
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
}
