package io.intino.alexandria.framework.box.displays.providers;

import io.intino.alexandria.foundation.activity.Resource;
import io.intino.alexandria.framework.box.displays.AlexandriaElementDisplay;
import io.intino.alexandria.framework.box.displays.AlexandriaStampDisplay;
import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.model.mold.Block;
import io.intino.alexandria.framework.box.model.Mold;
import io.intino.alexandria.framework.box.model.mold.Stamp;
import io.intino.alexandria.framework.box.schemas.ElementOperationParameters;
import io.intino.alexandria.framework.box.schemas.SaveItemParameters;

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

