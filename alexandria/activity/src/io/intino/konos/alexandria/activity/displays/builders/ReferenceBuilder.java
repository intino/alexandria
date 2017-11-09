package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.displays.ElementView;
import io.intino.konos.alexandria.activity.model.AbstractView;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.RenderCatalog;
import io.intino.konos.alexandria.activity.model.renders.RenderDisplay;
import io.intino.konos.alexandria.activity.model.renders.RenderMold;
import io.intino.konos.alexandria.activity.schemas.Reference;
import io.intino.konos.alexandria.activity.schemas.ReferenceProperty;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ReferenceBuilder {

    public static Reference build(String name, String label) {
        return new Reference().name(name).label(label);
    }

    public static Reference build(AbstractView view) {
        String type = typeOf(view);
        Reference result = new Reference().name(view.name()).label(view.label());
        result.referencePropertyList().add(new ReferenceProperty().name("type").value(type));
        return result;
    }

    public static Reference build(Element element) {
        return new Reference().name(element.name()).label(element.label());
    }

    public static Reference build(ElementView view) {
        return new Reference().name(view.name()).label(view.label()).referencePropertyList(singletonList(new ReferenceProperty().name("type").value(view.type())));
    }

    public static List<Reference> buildCatalogList(List<Catalog> catalogList) {
        return catalogList.stream().map(ReferenceBuilder::build).collect(toList());
    }

    public static List<Reference> buildCatalogViewList(List<ElementView> viewList) {
        return viewList.stream().map(ReferenceBuilder::build).collect(toList());
    }

    private static String typeOf(AbstractView view) {
        if (! (view instanceof View)) return "";

        ElementRender render = ((View) view).render();
        if (render instanceof RenderMold) return "custom-view";
        if (render instanceof RenderCatalog) return "catalog-view";
        if (render instanceof RenderDisplay) return "display-view";

        return "";
    }

}
