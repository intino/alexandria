package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.framework.box.model.Element;
import io.intino.alexandria.framework.box.model.Item;

public interface ElementDisplayManager {
	<E extends AlexandriaElementDisplay> E openElement(String label);
	<E extends AlexandriaElementDisplay> E createElement(Element element, Item target);
	<E extends AlexandriaElementDisplay> E displayWithLabel(String elementLabel);
}
