package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;

public interface ElementDisplayManager {
	<E extends AlexandriaElementDisplay> E openElement(String label);
	<E extends AlexandriaElementDisplay> E openElement(String label, String ownerId);
	<E extends AlexandriaElementDisplay> E createElement(Element element, Item target);
	void removeElement(Item item);
	<E extends AlexandriaElementDisplay> E displayWithKey(String elementLabel);
}
