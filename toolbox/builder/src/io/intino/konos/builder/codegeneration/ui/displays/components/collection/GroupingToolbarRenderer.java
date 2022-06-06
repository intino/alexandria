package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents.Grouping;
import io.intino.konos.model.CatalogComponents.GroupingToolbar;

import java.util.List;

public class GroupingToolbarRenderer extends ComponentRenderer<GroupingToolbar> {

	public GroupingToolbarRenderer(CompilationContext compilationContext, GroupingToolbar component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.groupings());
	}

	private void addBinding(FrameBuilder builder, List<Grouping> groupings) {
		if (groupings.size() <= 0) return;
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		groupings.forEach(c -> result.add("grouping", nameOf(c)));
		builder.add("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("groupingToolbar", "");
	}

}
