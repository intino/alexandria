package io.intino.konos.alexandria.activity.box.displays.providers;

import io.intino.konos.alexandria.activity.box.Resource;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaStampDisplay;
import io.intino.konos.alexandria.activity.box.model.Item;
import io.intino.konos.alexandria.activity.box.model.TimeRange;
import io.intino.konos.alexandria.activity.box.model.mold.Block;
import io.intino.konos.alexandria.activity.box.model.Mold;
import io.intino.konos.alexandria.activity.box.model.mold.Stamp;
import io.intino.konos.alexandria.activity.box.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.box.schemas.SaveItemParameters;

import java.util.List;

public interface ItemDisplayProvider {
	Mold mold();
	Item item(String id);
	TimeRange range();

	List<Block> blocks(Mold mold);
	List<Stamp> stamps(Mold mold);
	Stamp stamp(Mold mold, String stampName);

	AlexandriaStampDisplay display(String name);
	AlexandriaElementDisplay openElement(String label);

	void executeOperation(ElementOperationParameters params, List<Item> items);
	Resource downloadOperation(ElementOperationParameters params, List<Item> items);
	void saveItem(SaveItemParameters params, Item item);
}

