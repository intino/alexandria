package io.intino.konos.alexandria.ui.displays.builders;

import io.intino.konos.alexandria.ui.displays.AlexandriaElementViewDefinition;
import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.panel.PanelView;
import io.intino.konos.alexandria.ui.model.renders.RenderCatalogs;
import io.intino.konos.alexandria.ui.model.renders.RenderDisplay;
import io.intino.konos.alexandria.ui.model.renders.RenderMold;
import io.intino.konos.alexandria.ui.model.views.ContainerView;
import io.intino.konos.alexandria.ui.model.views.container.Container;
import io.intino.konos.alexandria.ui.model.views.container.MoldContainer;
import io.intino.konos.alexandria.ui.schemas.Reference;
import io.intino.konos.alexandria.ui.schemas.ReferenceProperty;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ReferenceBuilder {

    public static Reference build(String name, String label) {
        return new Reference().name(name).label(label);
    }

    public static Reference build(View view) {
        String type = typeOf(view);
        String layout = layoutOf(view);
        Reference result = new Reference().name(view.name()).label(view.label());
        result.referencePropertyList().add(new ReferenceProperty().name("type").value(type));
        result.referencePropertyList().add(new ReferenceProperty().name("layout").value(layout));
        return result;
    }

    public static Reference build(Element element) {
        return new Reference().name(element.name()).label(element.label());
    }

    public static Reference build(AlexandriaElementViewDefinition view) {
        return new Reference().name(view.name()).label(view.label()).referencePropertyList(singletonList(new ReferenceProperty().name("type").value(view.type())));
    }

    public static List<Reference> buildCatalogList(List<Catalog> catalogList) {
        return catalogList.stream().map(ReferenceBuilder::build).collect(toList());
    }

    public static List<Reference> buildCatalogViewList(List<View> viewList) {
        return viewList.stream().map(ReferenceBuilder::build).collect(toList());
    }

    private static String typeOf(View view) {
        if (! (view instanceof ContainerView)) return "";

        Container container = ((ContainerView) view).container();
        if (container instanceof RenderMold) return "custom-view";
        if (container instanceof RenderCatalogs) return "catalog-view";
        if (container instanceof RenderDisplay) return "display-view";

        return "";
    }

    private static String layoutOf(View view) {
        View.Layout layout = view.layout();

        if (layout == View.Layout.Tab) return "tab";
        if (layout == View.Layout.LeftFixed) return "left-fixed";
        if (layout == View.Layout.RightFixed) return "right-fixed";

        return "";
    }
}
