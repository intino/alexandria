package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.CatalogComponents.Sorting;

public class SortingRenderer extends BindingCollectionRenderer<Sorting> {

	public SortingRenderer(CompilationContext compilationContext, Sorting component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collections());
		addAddressableMethod(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		addOrderByProperties(properties);
		addAddressableMethod(properties);
		return properties;
	}

	private void addOrderByProperties(FrameBuilder properties) {
		if (!element.isOrderBy()) return;
		Sorting.OrderBy orderBy = element.asOrderBy();
		properties.add("mode", orderBy.mode() == Sorting.OrderBy.Mode.Ascending ? "asc" : "desc");
		properties.add("align", orderBy.align().name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("sorting", "");
	}

	private void addAddressableMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Sorting.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		Sorting.Addressable addressable = element.asAddressable();
		builder.add("path", addressable.addressableResource() != null ? addressable.addressableResource().path() : "");
	}
}
