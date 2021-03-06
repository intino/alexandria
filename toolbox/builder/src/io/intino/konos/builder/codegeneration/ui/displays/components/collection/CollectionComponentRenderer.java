package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.Component;

import java.util.List;

public abstract class CollectionComponentRenderer<C extends Component> extends ComponentRenderer<C> {

	public CollectionComponentRenderer(CompilationContext compilationContext, C component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("width", width() != -1 ? width() : defaultWidth());
		int position = position();
		if (position == 0) result.add("paddingLeft", "paddingLeft");
		result.add("paddingSize", position == 0 ? "20" : "10");
		result.add("hidden", hidden().name());
		return result;
	}

	private CatalogComponents.Collection.Mold.Hidden hidden() {
		return element.core$().ownerAs(CatalogComponents.Collection.Mold.class).hidden();
	}

	private int width() {
		return element.core$().ownerAs(CatalogComponents.Collection.Mold.class).item().width();
	}

	int defaultWidth() {
		CatalogComponents.Collection owner = element.core$().ownerAs(CatalogComponents.Collection.class);
		if (owner == null) return 100;
		return Math.round(100/owner.moldList().size());
	}

	int position() {
		CatalogComponents.Collection owner = element.core$().ownerAs(CatalogComponents.Collection.class);
		CatalogComponents.Collection.Mold mold = element.core$().ownerAs(CatalogComponents.Collection.Mold.class);
		if (owner == null) return 0;
		List<CatalogComponents.Collection.Mold> molds = owner.moldList();
		for (int i = 0; i< molds.size(); i++)
			if (molds.get(i).name$().equals(mold.name$())) return i;
		return 0;
	}

}
