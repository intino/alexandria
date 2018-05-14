package io.intino.konos.alexandria.ui.displays.providers;

import io.intino.konos.alexandria.ui.Resource;
import io.intino.konos.alexandria.ui.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.ui.displays.AlexandriaNavigator;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Mold;
import io.intino.konos.alexandria.ui.model.TimeRange;
import io.intino.konos.alexandria.ui.model.mold.Block;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.schemas.ChangeItemParameters;
import io.intino.konos.alexandria.ui.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.ui.schemas.ValidateItemParameters;

import java.util.List;

public interface ItemDisplayProvider {
	Mold mold();
	Item item(String id);
	TimeRange range();

	List<Block> blocks(Mold mold);
	List<Stamp> stamps(Mold mold);
	Stamp stamp(Mold mold, String stampName);

	<D extends AlexandriaElementDisplay> D openElement(String label);
	<N extends AlexandriaNavigator> void configureTemporalNavigator(N navigator);

	void executeOperation(ElementOperationParameters params, List<Item> items);
	Resource downloadOperation(ElementOperationParameters params, List<Item> items);
	void changeItem(Item item, ChangeItemParameters parameters);
	void validateItem(Item item, ValidateItemParameters parameters);
}

