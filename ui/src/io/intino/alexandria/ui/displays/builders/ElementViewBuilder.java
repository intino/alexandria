package io.intino.alexandria.ui.displays.builders;

import io.intino.alexandria.ui.displays.providers.ElementViewDisplayProvider;
import io.intino.alexandria.ui.model.Element;
import io.intino.alexandria.ui.model.Toolbar;
import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.toolbar.*;
import io.intino.alexandria.ui.model.view.container.CollectionContainer;
import io.intino.alexandria.ui.model.view.container.Container;
import io.intino.alexandria.ui.model.view.container.MapContainer;
import io.intino.alexandria.ui.schemas.Center;
import io.intino.alexandria.ui.schemas.ElementView;
import io.intino.alexandria.ui.schemas.Property;
import io.intino.alexandria.ui.schemas.Zoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

public class ElementViewBuilder {

    public static ElementView build(View view, ElementViewDisplayProvider provider) {
        ElementView result = new ElementView().name(view.name()).label(view.label()).mold(view.mold().type());
        Element element = provider.element();
        Toolbar toolbar = element.toolbar();

        if (toolbar != null)
            result.toolbar(buildToolbar(toolbar));

        result.embeddedElement(provider.embedded());
        result.type(view.getClass().getSimpleName());
        result.width(view.width());
        result.canSearch(toolbar != null && toolbar.canSearch());
        result.selectionEnabledByDefault(provider.selectionEnabledByDefault());
        result.emptyMessage(emptyMessage(view));
        addMapViewProperties(result, view);

        return result;
    }

    private static String emptyMessage(View view) {
        Container container = view.container();
        if (!(container instanceof CollectionContainer)) return "";
        String message = ((CollectionContainer) container).noRecordsMessage();
        return message != null ? message : "";
    }

    private static io.intino.alexandria.ui.schemas.Toolbar buildToolbar(Toolbar toolbar) {
        io.intino.alexandria.ui.schemas.Toolbar result = new io.intino.alexandria.ui.schemas.Toolbar();
        toolbar.operations().forEach(op -> result.operationList().add(buildOperation(op)));
        return result;
    }

    private static io.intino.alexandria.ui.schemas.Operation buildOperation(Operation operation) {
        io.intino.alexandria.ui.schemas.Operation result = new io.intino.alexandria.ui.schemas.Operation().title(operation.title()).name(operation.name());
        result.type(type(operation));
        result.mode(operation.mode().toString());
        result.icon(operation.alexandriaIcon());
        result.when(when(operation));
        result.propertyList(propertiesOf(operation));

        if (operation instanceof Task) result.confirmText(((Task)operation).confirmText());
        if (operation instanceof TaskSelection) result.confirmText(((TaskSelection)operation).confirmText());

        return result;
    }

    private static String when(Operation operation) {
        if ((operation instanceof TaskSelection) || (operation instanceof ExportSelection) ||
            (operation instanceof DownloadSelection) || (operation instanceof GroupingSelection) ||
            (operation instanceof OpenCatalogSelection))
            return "Selection";
        return "Always";
    }

    private static List<Property> propertiesOf(Operation operation) {

        if (operation instanceof Download)
            return singletonList(propertyOf("options", String.join(",", ((Download)operation).options())));

        if (operation instanceof DownloadSelection)
            return singletonList(propertyOf("options", String.join(",", ((DownloadSelection)operation).options())));

        if (operation instanceof Export) {
            Export export = (Export) operation;
            return new ArrayList<Property>() {{
                add(propertyOf("from", String.valueOf(export.from().toEpochMilli())));
                add(propertyOf("to", String.valueOf(export.to().toEpochMilli())));
            }};
        }

        if (operation instanceof ExportSelection) {
            ExportSelection exportSelection = (ExportSelection) operation;
            return new ArrayList<Property>() {{
                add(propertyOf("from", String.valueOf(exportSelection.from().toEpochMilli())));
                add(propertyOf("to", String.valueOf(exportSelection.to().toEpochMilli())));
            }};
        }

        return Collections.emptyList();
    }

    private static Property propertyOf(String name, String value) {
        return new Property().name(name).value(value);
    }

    private static String type(Operation operation) {
        if (operation instanceof Download || operation instanceof DownloadSelection) return "download";
        if (operation instanceof Export || operation instanceof ExportSelection) return "export";
        if (operation instanceof OpenCatalog || operation instanceof OpenCatalogSelection) return "open-catalog";
        if (operation instanceof OpenDialog) return "open-dialog";
        if (operation instanceof GroupingSelection) return "grouping";
        return "operation";
    }

    private static void addMapViewProperties(ElementView result, View view) {
        Container container = view.container();
        if (! (container instanceof MapContainer)) return;

        MapContainer mapContainer = (MapContainer)container;
        MapContainer.Zoom zoom = mapContainer.zoom();
        MapContainer.Center center = mapContainer.center();

        result.center(new Center().latitude(center.latitude()).longitude(center.longitude()));
        result.zoom(new Zoom().min(zoom.min()).max(zoom.max()).defaultValue(zoom.defaultZoom()));
    }

}
