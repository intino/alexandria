package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;

public class MagazineItemMold extends AbstractMagazineItemMold<AlexandriaUiBox> {

	public MagazineItemMold(AlexandriaUiBox box) {
		super(box);
	}

	@Override
	public void refresh() {
		super.refresh();
		label.value(item().label());
	}
}
