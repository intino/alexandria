package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.ChildComponents;
import io.intino.konos.model.graph.Component;
import org.siani.itrules.model.Frame;

import java.util.List;

public abstract class CollectionComponentRenderer<C extends Component> extends ComponentRenderer<C> {

	public CollectionComponentRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addSlot("width", width() != -1 ? width() : defaultWidth());
		int position = position();
		if (position == 0) result.addSlot("marginLeft", "marginLeft");
		result.addSlot("marginSize", position == 0 ? "20" : "10");
		return result;
	}

	private int width() {
		return element.core$().ownerAs(ChildComponents.Collection.Mold.class).item().width();
	}

	int defaultWidth() {
		ChildComponents.Collection owner = element.core$().ownerAs(ChildComponents.Collection.class);
		if (owner == null) return 100;
		return Math.round(100/owner.moldList().size());
	}

	int position() {
		ChildComponents.Collection owner = element.core$().ownerAs(ChildComponents.Collection.class);
		ChildComponents.Collection.Mold mold = element.core$().ownerAs(ChildComponents.Collection.Mold.class);
		if (owner == null) return 0;
		List<ChildComponents.Collection.Mold> molds = owner.moldList();
		for (int i = 0; i< molds.size(); i++)
			if (molds.get(i).name$().equals(mold.name$())) return i;
		return 0;
	}

}
