package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;

public interface ElementDisplayManager {
	<E extends AlexandriaElementDisplay> E openElement(String label);
	<E extends AlexandriaElementDisplay> E createElement(Element element, Item target);
	void removeElement(Item item);
	<E extends AlexandriaElementDisplay> E displayWithLabel(String elementLabel);
}
