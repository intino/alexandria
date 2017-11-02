package io.intino.alexandria.framework.box.displays.builders;

import io.intino.alexandria.framework.box.displays.ElementView;
import io.intino.alexandria.framework.box.model.AbstractView;
import io.intino.alexandria.framework.box.model.Element;
import io.intino.alexandria.framework.box.model.ElementRender;
import io.intino.alexandria.framework.box.model.Catalog;
import io.intino.alexandria.framework.box.model.panel.View;
import io.intino.alexandria.framework.box.model.renders.RenderCatalog;
import io.intino.alexandria.framework.box.model.renders.RenderDisplay;
import io.intino.alexandria.framework.box.model.renders.RenderMold;
import io.intino.alexandria.framework.box.schemas.Reference;
import io.intino.alexandria.framework.box.schemas.ReferenceProperty;

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
        if (render instanceof RenderDisplay) return "olap-view";

        return "";
    }

}
