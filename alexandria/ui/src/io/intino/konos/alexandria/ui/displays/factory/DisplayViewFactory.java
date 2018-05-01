package io.intino.konos.alexandria.ui.displays.factory;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.*;
import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.views.*;
import io.intino.konos.alexandria.ui.model.views.container.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class DisplayViewFactory {
	private static Map<Class<? extends View>, BiFunction<Box, View, AlexandriaElementView>> viewMap = new HashMap<>();
	private static Map<Class<? extends Container>, BiFunction<Box, View, AlexandriaContainerView>> containerViewMap = new HashMap<>();

	public static <V extends AlexandriaElementView> V build(Box box, View view) {
		AlexandriaElementView result = null;

		if (view instanceof ContainerView)
			result = buildContainerView(box, ((ContainerView)view));
		else
			result = viewMap.containsKey(view.getClass()) ? viewMap.get(view.getClass()).apply(box, view) : null;

		if (result != null)
			result.view(view);

		return (V) result;
	}

	private static AlexandriaContainerView buildContainerView(Box box, ContainerView view) {
		Container container = view.container();
		return containerViewMap.containsKey(container.getClass()) ? containerViewMap.get(container.getClass()).apply(box, view) : null;
	}

	private static AlexandriaElementView buildCatalogMagazineView(Box box, View view) {
		return new AlexandriaCatalogMagazineView(box);
	}

	private static AlexandriaElementView buildCatalogListView(Box box, View view) {
		return new AlexandriaCatalogListView(box);
	}

	private static AlexandriaElementView buildCatalogMapView(Box box, View view) {
		return new AlexandriaCatalogMapView(box);
	}

	private static AlexandriaContainerView buildMoldViewDisplay(Box box, View view) {
		return new AlexandriaContainerViewMold(box);
	}

	private static AlexandriaContainerViewCatalog buildCatalogViewDisplay(Box box, View view) {
		return new AlexandriaContainerViewCatalog(box);
	}

	private static AlexandriaContainerViewPanel buildPanelViewDisplay(Box box, View view) {
		return new AlexandriaContainerViewPanel(box);
	}

	private static AlexandriaContainerViewDisplay buildDisplayViewDisplay(Box box, View view) {
		return new AlexandriaContainerViewDisplay(box);
	}

	private static AlexandriaContainerViewSet buildSetViewDisplay(Box box, View view) {
		return new AlexandriaContainerViewSet(box);
	}

	static {
		viewMap.put(MagazineView.class, DisplayViewFactory::buildCatalogMagazineView);
		viewMap.put(ListView.class, DisplayViewFactory::buildCatalogListView);
		viewMap.put(GridView.class, DisplayViewFactory::buildCatalogListView);
		viewMap.put(MapView.class, DisplayViewFactory::buildCatalogMapView);

		containerViewMap.put(MoldContainer.class, DisplayViewFactory::buildMoldViewDisplay);
		containerViewMap.put(CatalogContainer.class, DisplayViewFactory::buildCatalogViewDisplay);
		containerViewMap.put(PanelContainer.class, DisplayViewFactory::buildPanelViewDisplay);
		containerViewMap.put(DisplayContainer.class, DisplayViewFactory::buildDisplayViewDisplay);
		containerViewMap.put(SetContainer.class, DisplayViewFactory::buildSetViewDisplay);
	}
}
