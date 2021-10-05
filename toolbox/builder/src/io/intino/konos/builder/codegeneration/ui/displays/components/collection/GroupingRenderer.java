package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.CatalogComponents.Grouping;
import io.intino.konos.model.graph.OtherComponents;

import java.util.List;

public class GroupingRenderer extends BindingCollectionRenderer<Grouping> {

	public GroupingRenderer(CompilationContext compilationContext, Grouping component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collections());
		addAttachedTo(builder);
		addAddressableMethod(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (!element.isComboBox()) properties.add("pageSize", element.pageSize());
		addComboBoxProperties(properties);
		addAddressableProperties(properties);
		return properties;
	}

	private void addComboBoxProperties(FrameBuilder builder) {
		if (!element.isComboBox()) return;
		String placeholder = element.asComboBox().placeholder();
		if (placeholder != null && !placeholder.isEmpty()) builder.add("placeholder", placeholder);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("grouping", "");
	}

	private void addAttachedTo(FrameBuilder builder) {
		if (!element.isAttachedTo()) return;
		FrameBuilder result = new FrameBuilder("attachedTo", type()).add("name", nameOf(element));
		result.add("grouping", nameOf(element.asAttachedTo().grouping()));
		builder.add("attachedTo", result);
	}

	private void addAddressableMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Grouping.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		Grouping.Addressable addressable = element.asAddressable();
		builder.add("path", addressable.addressableResource() != null ? addressable.addressableResource().path() : "");
	}

}
