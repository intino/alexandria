package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.items.Magazine1Mold;
import io.intino.alexandria.ui.displays.items.Magazine2Mold;
import io.intino.alexandria.ui.documentation.model.Datasources;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

public class MagazineExamplesMold extends AbstractMagazineExamplesMold<AlexandriaUiBox> {

	public MagazineExamplesMold(AlexandriaUiBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		init(magazine1, Datasources.itemDatasource());
		init(magazine2, Datasources.personDatasource());
	}

	@Override
	public void refresh() {
		super.refresh();
		magazine1.reload();
		magazine2.reload();
	}

	private void init(io.intino.alexandria.ui.displays.components.Magazine magazine, PageDatasource datasource) {
		magazine.source(datasource);
		magazine.onAddItem(this::onAddItem);
	}

	private void onAddItem(AddCollectionItemEvent event) {
		if (event.component() instanceof Magazine1Mold) ((Magazine1Mold) event.component()).stamp.item(event.item());
		else if (event.component() instanceof Magazine2Mold) ((Magazine2Mold) event.component()).stamp.item(event.item());
	}
}
