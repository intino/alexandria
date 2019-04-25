package io.intino.alexandria.ui.displays.components.toolbar;

import java.util.List;

public interface SelectionOperation {
	void selectedItems(List<Object> items);
	voy por aqui... estoy con la idea de una toolbar que enganche con una colección para las operaciones de seleccion
		la toolbar hace un bindTo a la colección para enterarse de cuando cambia la selección de la colección
}
