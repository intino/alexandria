package io.intino.konos.server.activity.displays.elements.providers;

import io.intino.konos.server.activity.Resource;
import io.intino.konos.server.activity.displays.elements.ElementDisplay;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.molds.model.Block;
import io.intino.konos.server.activity.displays.molds.model.Mold;
import io.intino.konos.server.activity.displays.molds.model.Stamp;
import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.schemas.ElementOperationParameters;
import io.intino.konos.server.activity.displays.schemas.SaveItemParameters;

import java.util.List;

public interface ItemDisplayProvider {
	Mold mold();
	Item item(String id);
	TimeRange range();

	List<Block> blocks(Mold mold);
	List<Stamp> stamps(Mold mold);
	Stamp stamp(Mold mold, String stampName);

	StampDisplay display(String name);
	ElementDisplay openElement(String label);

	void executeOperation(ElementOperationParameters params, List<Item> items);
	Resource downloadOperation(ElementOperationParameters params, List<Item> items);
	void saveItem(SaveItemParameters params, Item item);
}

