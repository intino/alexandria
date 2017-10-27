package io.intino.konos.server.activity.displays.elements;

import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.Item;

public interface ElementDisplayManager {
	<E extends ElementDisplay> E openElement(String label);
	<E extends ElementDisplay> E createElement(Element element, Item target);
	<E extends ElementDisplay> E displayWithLabel(String elementLabel);
}
