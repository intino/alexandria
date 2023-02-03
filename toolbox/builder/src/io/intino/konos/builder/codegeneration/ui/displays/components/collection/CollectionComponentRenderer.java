package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents;
import io.intino.konos.model.Component;

import java.util.List;

public abstract class CollectionComponentRenderer<C extends Component> extends ComponentRenderer<C> {

	public CollectionComponentRenderer(CompilationContext compilationContext, C component, RendererWriter provider) {
		super(compilationContext, component, provider);
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

	private CatalogComponents.Moldable.Mold.Hidden hidden() {
		if (element.core$().ownerAs(CatalogComponents.Moldable.class) == null) return CatalogComponents.Moldable.Mold.Hidden.Never;
		return element.core$().ownerAs(CatalogComponents.Moldable.Mold.class).hidden();
	}

	private int width() {
		if (element.core$().ownerAs(CatalogComponents.Moldable.class) == null) return 0;
		return element.core$().ownerAs(CatalogComponents.Moldable.Mold.class).item().width();
	}

	int defaultWidth() {
		CatalogComponents.Moldable owner = element.core$().ownerAs(CatalogComponents.Moldable.class);
		if (owner == null) return 100;
		return Math.round(100/owner.moldList().size());
	}

	int position() {
		CatalogComponents.Moldable owner = element.core$().ownerAs(CatalogComponents.Moldable.class);
		if (owner == null) return 0;
		CatalogComponents.Moldable.Mold mold = element.core$().ownerAs(CatalogComponents.Moldable.Mold.class);
		if (owner == null) return 0;
		List<CatalogComponents.Moldable.Mold> molds = owner.moldList();
		for (int i = 0; i< molds.size(); i++)
			if (molds.get(i).name$().equals(mold.name$())) return i;
		return 0;
	}

}
