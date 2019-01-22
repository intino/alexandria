package io.intino.alexandria.ui.displays.builders;

import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.Element;
import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.view.container.*;
import io.intino.alexandria.ui.schemas.Reference;
import io.intino.alexandria.ui.schemas.ReferenceProperty;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ReferenceBuilder {

    public static Reference build(String name, String label) {
        return new Reference().name(name).label(label);
    }

    public static Reference build(Element element) {
        return new Reference().name(element.name()).label(element.label());
    }

    public static Reference build(View view) {
        String type = typeOf(view);
        String layout = layoutOf(view);
        Reference result = new Reference().name(view.name()).label(view.label());
        result.referencePropertyList().add(new ReferenceProperty().name("type").value(type));
        result.referencePropertyList().add(new ReferenceProperty().name("layout").value(layout));
        return result;
    }

    public static List<Reference> buildCatalogList(List<Catalog> catalogList) {
        return catalogList.stream().map(ReferenceBuilder::build).collect(toList());
    }

    public static List<Reference> buildCatalogViewList(List<View> viewList) {
        return viewList.stream().map(ReferenceBuilder::build).collect(toList());
    }

    private static String typeOf(View view) {
        Container container = view.container();

        if (container instanceof ListContainer) return "view-container-list";
        if (container instanceof GridContainer) return "view-container-grid";
        if (container instanceof MagazineContainer) return "view-container-magazine";
        if (container instanceof MapContainer) return "view-container-map";
        if (container instanceof MoldContainer) return "view-container-mold";
        if (container instanceof CatalogContainer) return "view-container-catalog";
        if (container instanceof PanelContainer) return "view-container-panel";
        if (container instanceof DisplayContainer) return "view-container-display";
        if (container instanceof SetContainer) return "view-container-set";

        return view.getClass().getSimpleName();
    }

    private static String layoutOf(View view) {
        View.Layout layout = view.layout();

        if (layout == View.Layout.Tab) return "tab";
        if (layout == View.Layout.LeftFixed) return "left-fixed";
        if (layout == View.Layout.Summary) return "summary";
        if (layout == View.Layout.RightFixed) return "right-fixed";

        return "";
    }
}
