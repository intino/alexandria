package io.intino.alexandria.ui.displays.factory;

import io.intino.alexandria.ui.displays.*;
import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.view.container.*;
import io.intino.konos.framework.Box;
import io.intino.alexandria.ui.displays.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class DisplayViewFactory {
	private static Map<Class<? extends Container>, BiFunction<Box, View, AlexandriaElementView>> viewMap = new HashMap<>();

	public static <V extends AlexandriaElementView> V build(Box box, View view) {
		Container container = view.container();
		AlexandriaElementView result = viewMap.containsKey(container.getClass()) ? viewMap.get(container.getClass()).apply(box, view) : null;

		if (result != null)
			result.view(view);

		return (V) result;
	}

	private static AlexandriaElementView buildViewContainerMagazine(Box box, View view) {
		return new AlexandriaViewContainerMagazine(box);
	}

	private static AlexandriaElementView buildViewContainerList(Box box, View view) {
		return new AlexandriaViewContainerList(box);
	}

	private static AlexandriaElementView buildViewContainerMap(Box box, View view) {
		return new AlexandriaViewContainerMap(box);
	}

	private static AlexandriaViewContainer buildViewContainerMold(Box box, View view) {
		return new AlexandriaViewContainerMold(box);
	}

	private static AlexandriaViewContainerCatalog buildViewContainerCatalog(Box box, View view) {
		return new AlexandriaViewContainerCatalog(box);
	}

	private static AlexandriaViewContainerPanel buildViewContainerPanel(Box box, View view) {
		return new AlexandriaViewContainerPanel(box);
	}

	private static AlexandriaViewContainerDisplay buildViewContainerDisplay(Box box, View view) {
		return new AlexandriaViewContainerDisplay(box);
	}

	private static AlexandriaViewContainerSet buildViewContainerSet(Box box, View view) {
		return new AlexandriaViewContainerSet(box);
	}

	static {
		viewMap.put(MagazineContainer.class, DisplayViewFactory::buildViewContainerMagazine);
		viewMap.put(ListContainer.class, DisplayViewFactory::buildViewContainerList);
		viewMap.put(GridContainer.class, DisplayViewFactory::buildViewContainerList);
		viewMap.put(MapContainer.class, DisplayViewFactory::buildViewContainerMap);
		viewMap.put(MoldContainer.class, DisplayViewFactory::buildViewContainerMold);
		viewMap.put(CatalogContainer.class, DisplayViewFactory::buildViewContainerCatalog);
		viewMap.put(PanelContainer.class, DisplayViewFactory::buildViewContainerPanel);
		viewMap.put(DisplayContainer.class, DisplayViewFactory::buildViewContainerDisplay);
		viewMap.put(SetContainer.class, DisplayViewFactory::buildViewContainerSet);
	}
}
