package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.activity.box.model.Element;
import io.intino.konos.alexandria.activity.box.model.Item;

public interface ElementDisplayManager {
	<E extends AlexandriaElementDisplay> E openElement(String label);
	<E extends AlexandriaElementDisplay> E createElement(Element element, Item target);
	<E extends AlexandriaElementDisplay> E displayWithLabel(String elementLabel);
}
