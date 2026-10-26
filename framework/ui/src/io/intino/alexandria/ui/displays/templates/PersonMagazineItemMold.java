package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;

public class PersonMagazineItemMold extends AbstractPersonMagazineItemMold<AlexandriaUiBox> {

	public PersonMagazineItemMold(AlexandriaUiBox box) {
		super(box);
	}

	@Override
	public void refresh() {
		super.refresh();
		firstName.value(item().firstName());
		lastName.value(item().lastName());
	}
}
