package io.intino.konos.alexandria.activity.box.displays.builders;

import io.intino.konos.alexandria.activity.box.model.AbstractView;
import io.intino.konos.alexandria.activity.box.model.catalog.views.MapView;
import io.intino.konos.alexandria.activity.box.model.toolbar.*;
import io.intino.konos.alexandria.activity.box.model.toolbar.GroupingSelection;
import io.intino.konos.alexandria.activity.box.schemas.*;
import io.intino.konos.alexandria.activity.box.schemas.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ElementViewBuilder {

    public static ElementView build(io.intino.konos.alexandria.activity.box.displays.ElementView view) {
        ElementView result = new ElementView().name(view.name()).label(view.label());

        if (view.toolbar() != null)
            result.toolbar(buildToolbar(view.toolbar()));

        result.embeddedElement(view.embeddedElement());
        result.mold(MoldBuilder.build(view.mold()));
        result.type(view.type());
        result.width(view.width());
        result.canSearch(view.canSearch());
        result.canCreateClusters(view.canCreateClusters());
        result.clusters(view.clusters());
        result.emptyMessage(view.emptyMessage());
        addMapViewProperties(result, view);
        return result;
    }

    private static Toolbar buildToolbar(io.intino.konos.alexandria.activity.box.model.Toolbar toolbar) {
        Toolbar result = new Toolbar();
        toolbar.operations().forEach(op -> result.operationList().add(buildOperation(op)));
        return result;
    }

    private static Operation buildOperation(io.intino.konos.alexandria.activity.box.model.toolbar.Operation operation) {
        Operation result = new Operation().title(operation.title()).name(operation.name());

        result.type(type(operation));
        result.icon(operation.sumusIcon());
        result.when(when(operation));
        result.propertyList(propertiesOf(operation));

        return result;
    }

    private static String when(io.intino.konos.alexandria.activity.box.model.toolbar.Operation operation) {
        if ((operation instanceof TaskSelection) || (operation instanceof ExportSelection) ||
            (operation instanceof DownloadSelection) || (operation instanceof GroupingSelection))
            return "Selection";
        return "Always";
    }

    private static List<Property> propertiesOf(io.intino.konos.alexandria.activity.box.model.toolbar.Operation operation) {

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

    private static String type(io.intino.konos.alexandria.activity.box.model.toolbar.Operation operation) {
        if (operation instanceof Download || operation instanceof DownloadSelection) return "download";
        if (operation instanceof Export || operation instanceof ExportSelection) return "export";
        if (operation instanceof OpenDialog) return "open-dialog";
        if (operation instanceof GroupingSelection) return "grouping";
        return "operation";
    }

    public static List<ElementView> buildList(List<io.intino.konos.alexandria.activity.box.displays.ElementView> viewList) {
        return viewList.stream().map(ElementViewBuilder::build).collect(toList());
    }

    private static void addMapViewProperties(ElementView result, io.intino.konos.alexandria.activity.box.displays.ElementView view) {
        AbstractView rawView = view.raw();
        if (! (rawView instanceof MapView)) return;

        MapView mapView = (MapView)rawView;
        MapView.Zoom zoom = mapView.zoom();
        MapView.Center center = mapView.center();

        result.center(new Center().latitude(center.latitude()).longitude(center.longitude()));
        result.zoom(new Zoom().min(zoom.min()).max(zoom.max()).defaultValue(zoom.defaultZoom()));
    }

}
