package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.model.AbstractView;
import io.intino.konos.alexandria.activity.model.catalog.views.MapView;
import io.intino.konos.alexandria.activity.model.toolbar.*;
import io.intino.konos.alexandria.activity.model.toolbar.GroupingSelection;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.schemas.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

public class ElementViewBuilder {

    public static ElementView build(io.intino.konos.alexandria.activity.displays.ElementView view) {
        ElementView result = new ElementView().name(view.name()).label(view.label()).mold(view.mold().type());

        if (view.toolbar() != null)
            result.toolbar(buildToolbar(view.toolbar()));

        result.embeddedElement(view.embeddedElement());
        result.type(view.type());
        result.width(view.width());
        result.canSearch(view.canSearch());
        result.canCreateClusters(view.canCreateClusters());
        result.selectionEnabledByDefault(view.selectionEnabledByDefault());
        result.clusters(view.clusters());
        result.emptyMessage(view.emptyMessage() != null ? view.emptyMessage() : "");
        addMapViewProperties(result, view);
        return result;
    }

    private static Toolbar buildToolbar(io.intino.konos.alexandria.activity.model.Toolbar toolbar) {
        Toolbar result = new Toolbar();
        toolbar.operations().forEach(op -> result.operationList().add(buildOperation(op)));
        return result;
    }

    private static Operation buildOperation(io.intino.konos.alexandria.activity.model.toolbar.Operation operation) {
        Operation result = new Operation().title(operation.title()).name(operation.name());

        result.type(type(operation));
        result.mode(operation.mode().toString());
        result.icon(operation.alexandriaIcon());
        result.when(when(operation));
        result.propertyList(propertiesOf(operation));

        if (operation instanceof Task) result.confirmText(((Task)operation).confirmText());
        if (operation instanceof TaskSelection) result.confirmText(((TaskSelection)operation).confirmText());

        return result;
    }

    private static String when(io.intino.konos.alexandria.activity.model.toolbar.Operation operation) {
        if ((operation instanceof TaskSelection) || (operation instanceof ExportSelection) ||
            (operation instanceof DownloadSelection) || (operation instanceof GroupingSelection) ||
            (operation instanceof OpenCatalogSelection))
            return "Selection";
        return "Always";
    }

    private static List<Property> propertiesOf(io.intino.konos.alexandria.activity.model.toolbar.Operation operation) {

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

    private static String type(io.intino.konos.alexandria.activity.model.toolbar.Operation operation) {
        if (operation instanceof Download || operation instanceof DownloadSelection) return "download";
        if (operation instanceof Export || operation instanceof ExportSelection) return "export";
        if (operation instanceof OpenCatalog || operation instanceof OpenCatalogSelection) return "open-catalog";
        if (operation instanceof OpenDialog) return "open-dialog";
        if (operation instanceof GroupingSelection) return "grouping";
        return "operation";
    }

    private static void addMapViewProperties(ElementView result, io.intino.konos.alexandria.activity.displays.ElementView view) {
        AbstractView rawView = view.raw();
        if (! (rawView instanceof MapView)) return;

        MapView mapView = (MapView)rawView;
        MapView.Zoom zoom = mapView.zoom();
        MapView.Center center = mapView.center();

        result.center(new Center().latitude(center.latitude()).longitude(center.longitude()));
        result.zoom(new Zoom().min(zoom.min()).max(zoom.max()).defaultValue(zoom.defaultZoom()));
    }

}
