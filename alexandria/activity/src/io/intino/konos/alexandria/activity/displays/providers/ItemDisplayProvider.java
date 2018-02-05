package io.intino.konos.alexandria.activity.displays.providers;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Mold;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.schemas.SaveItemParameters;

import java.util.List;

public interface ItemDisplayProvider {
	Mold mold();
	Item item(String id);
	TimeRange range();

	List<Block> blocks(Mold mold);
	List<Stamp> stamps(Mold mold);
	Stamp stamp(Mold mold, String stampName);

	AlexandriaElementDisplay openElement(String label);

	void executeOperation(ElementOperationParameters params, List<Item> items);
	Resource downloadOperation(ElementOperationParameters params, List<Item> items);
	void saveItem(SaveItemParameters params, Item item);
}

