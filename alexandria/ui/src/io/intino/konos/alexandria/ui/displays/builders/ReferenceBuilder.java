package io.intino.konos.alexandria.ui.displays.builders;

import io.intino.konos.alexandria.ui.displays.AlexandriaElementViewDefinition;
import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.ElementRender;
import io.intino.konos.alexandria.ui.model.panel.PanelView;
import io.intino.konos.alexandria.ui.model.renders.RenderCatalogs;
import io.intino.konos.alexandria.ui.model.renders.RenderDisplay;
import io.intino.konos.alexandria.ui.model.renders.RenderMold;
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

    public static List<Reference> buildCatalogViewList(List<AlexandriaElementViewDefinition> viewList) {
        return viewList.stream().map(ReferenceBuilder::build).collect(toList());
    }

    private static String typeOf(View view) {
        if (! (view instanceof PanelView)) return "";

        ElementRender render = ((PanelView) view).render();
        if (render instanceof RenderMold) return "custom-view";
        if (render instanceof RenderCatalogs) return "catalog-view";
        if (render instanceof RenderDisplay) return "display-view";

        return "";
    }

    private static String layoutOf(View view) {
        if (! (view instanceof PanelView)) return "";

        PanelView.Layout layout = ((PanelView) view).layout();
        if (layout == PanelView.Layout.Tab) return "tab";
        if (layout == PanelView.Layout.LeftFixed) return "left-fixed";
        if (layout == PanelView.Layout.RightFixed) return "right-fixed";

        return "";
    }
}
