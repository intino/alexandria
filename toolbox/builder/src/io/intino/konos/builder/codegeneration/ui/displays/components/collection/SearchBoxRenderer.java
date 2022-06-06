package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents.SearchBox;

public class SearchBoxRenderer extends BindingCollectionRenderer<SearchBox> {

	public SearchBoxRenderer(CompilationContext compilationContext, SearchBox component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collections());
		addAddressableMethod(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		String placeholder = element.placeholder();
		addAddressableProperties(result);
		if (placeholder != null && !placeholder.isEmpty()) result.add("placeholder", placeholder);
		if (element.showCountMessage()) result.add("showCountMessage", true);
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("searchbox", "");
	}

	private void addAddressableMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(SearchBox.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		SearchBox.Addressable addressable = element.asAddressable();
		builder.add("path", addressable.addressableResource() != null ? addressable.addressableResource().path() : "");
	}
}
