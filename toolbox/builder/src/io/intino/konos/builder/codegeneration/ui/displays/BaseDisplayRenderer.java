package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRendererFactory;
import io.intino.konos.builder.codegeneration.ui.passiveview.PassiveViewRenderer;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import io.intino.konos.model.graph.addressable.operationcomponents.AddressableTask;
import io.intino.konos.model.graph.dynamicloaded.DynamicLoadedComponent;
import io.intino.konos.model.graph.selectable.catalogcomponents.SelectableCollection;
import org.jetbrains.annotations.NotNull;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public abstract class BaseDisplayRenderer<D extends Display> extends PassiveViewRenderer<D> {
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();

	protected BaseDisplayRenderer(Settings settings, D display, TemplateProvider templateProvider, Target target) {
		super(settings, display, templateProvider, target);
	}

	@Override
	public void render() {
		if (element == null) return;
		String path = path(element);
		final String newDisplay = snakeCaseToCamelCase(element.name$());
		classes().put("Display#" + element.name$(), path + "." + newDisplay);
		FrameBuilder result = buildFrame();
		createPassiveViewFiles(result);
		write(result);
		if (element.isAccessible()) writeDisplaysFor(element.asAccessible(), buildFrame(true));
	}

	@Override
	public FrameBuilder buildFrame() {
		return buildFrame(false);
	}

	public FrameBuilder buildFrame(boolean accessible) {
		FrameBuilder result = super.buildFrame(accessible);
		result.add("display");
		result.add(typeOf(element));
		if (accessible) result.add("accessible");
		addParametrized(result);
		addExtends(result);
		addImports(result, accessible);
		addImplements(result);
		addMethods(result);
		addRenderTagFrames(result);
		addDecoratedFrames(result);
		result.add("componentType", element.components().stream().map(this::typeOf).distinct().map(type -> new FrameBuilder().add("componentType", type).toFrame()).toArray(Frame[]::new));
		if (element.parentDisplay() != null) addParent(element, result);
		if (!element.graph().schemaList().isEmpty())
			result.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()));
		if (element.isAccessible())
			result.add("parameter", element.asAccessible().parameters().stream().map(p -> new FrameBuilder("parameter", "accessible").add("value", p).toFrame()).toArray(Frame[]::new));
		return result;
	}

	private void addParametrized(FrameBuilder frame) {
		FrameBuilder result = new FrameBuilder("parametrized");
		result.add("name", element.name$());
		addGeneric(element, result);
		addDecoratedFrames(result);
		frame.add("parametrized", result.toFrame());
	}

	private void addExtends(FrameBuilder frame) {
		FrameBuilder result = new FrameBuilder("displayExtends");
		if (element.i$(Template.class)) result.add(Template.class.getSimpleName());
		if (element.i$(CatalogComponents.Collection.Mold.Item.class)) result.add(CatalogComponents.Collection.Mold.Item.class.getSimpleName());
		if (element.i$(PrivateComponents.Row.class)) result.add(PrivateComponents.Row.class.getSimpleName());
		addGeneric(element, result);
		result.add("type", typeOf(element));
		addDecoratedFrames(result);
		if (element.i$(Template.class)) {
			String modelClass = element.a$(Template.class).modelClass();
			result.add("modelClass", modelClass != null ? modelClass : "java.lang.Void");
		}
		if (element.i$(CatalogComponents.Collection.Mold.Item.class)) {
			String itemClass = element.a$(CatalogComponents.Collection.Mold.Item.class).core$().ownerAs(CatalogComponents.Collection.class).itemClass();
			result.add("itemClass", itemClass != null ? itemClass : "java.lang.Void");
		}
		if (element.i$(PrivateComponents.Row.class)) {
			String itemClass = element.a$(PrivateComponents.Row.class).items(0).core$().ownerAs(CatalogComponents.Collection.class).itemClass();
			result.add("itemClass", itemClass != null ? itemClass : "java.lang.Void");
		}
		result.add("name", nameOf(element));
		frame.add("displayExtends", result);
	}

	private void addImports(FrameBuilder frame, boolean accessible) {
		KonosGraph graph = element.graph();
		if (graph.templateList().size() > 0) frame.add("templatesImport", buildBaseFrame().add("templatesImport"));
		if (graph.blockList().size() > 0) frame.add("blocksImport", buildBaseFrame().add("blocksImport"));
		if (graph.itemsDisplays().size() > 0) frame.add("itemsImport", buildBaseFrame().add("itemsImport"));
		if (graph.rowsDisplays().size() > 0) frame.add("rowsImport", buildBaseFrame().add("rowsImport"));
		if (!ElementHelper.isRoot(componentOf(element)) || (element.isAccessible() && accessible)) frame.add("displayRegistration", displayRegistrationFrame(accessible));
		frame.add("requesterDirectory", typeOf(element).equalsIgnoreCase("Display") || typeOf(element).equalsIgnoreCase("Display") ? "." : "..");
		frame.add("notifierDirectory", typeOf(element).equalsIgnoreCase("Display") ? "." : "..");
	}

	@NotNull
	private FrameBuilder displayRegistrationFrame(boolean accessible) {
		FrameBuilder result = buildBaseFrame().add("displayRegistration");
		if (element.isAccessible() && accessible) result.add("accessible");
		return result.add("name", nameOf(element));
	}

	protected void addImplements(FrameBuilder frame) {
		if (element.i$(DynamicLoadedComponent.class)) frame.add("implements", new FrameBuilder("implements", DynamicLoadedComponent.class.getSimpleName()));
		if (element.i$(SelectableCollection.class)) frame.add("implements", new FrameBuilder("implements", SelectableCollection.class.getSimpleName()));
		if (element.i$(AddressableTask.class)) frame.add("implements", new FrameBuilder("implements", AddressableTask.class.getSimpleName()).add("name", nameOf(element)));
	}

	protected void addMethods(FrameBuilder frame) {
		if (element.i$(DynamicLoadedComponent.class)) {
			frame.add("baseMethod", "renderDynamicLoaded");
			frame.add("methods", new FrameBuilder("methods", DynamicLoadedComponent.class.getSimpleName()).add("loadTime", element.a$(DynamicLoadedComponent.class).loadTime().name()));
		}
		else if (element.i$(PrivateComponents.Row.class)) {
			frame.add("baseMethod", "renderRow");
		}
	}

	protected void addRenderTagFrames(FrameBuilder frame) {
		FrameBuilder renderTag = new FrameBuilder("renderTag");
		if (element.i$(Block.class)) {
			ComponentRenderer renderer = factory.renderer(settings, element.a$(Block.class), templateProvider, target);
			renderTag.add(Block.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		}
		else if (element.i$(Template.class)) {
			ComponentRenderer renderer = factory.renderer(settings, element.a$(Template.class), templateProvider, target);
			renderTag.add(Template.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		}
		else if (element.i$(PrivateComponents.Row.class)) {
			ComponentRenderer renderer = factory.renderer(settings, element.a$(PrivateComponents.Row.class), templateProvider, target);
			renderTag.add(PrivateComponents.Row.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		}
		else if (element.i$(CatalogComponents.Collection.Mold.Item.class)) {
			renderTag.add(CatalogComponents.Collection.Mold.Item.class.getSimpleName());
		}
		frame.add("renderTag", renderTag);
	}

	protected void addDecoratedFrames(FrameBuilder frame) {
		addDecoratedFrames(frame, ElementHelper.isRoot(element));
	}

	protected void addDecoratedFrames(FrameBuilder frame, boolean decorated) {
		boolean isAbstract = decorated && !element.i$(OtherComponents.Stamp.class);
		if (isAbstract) frame.add("abstract", "Abstract");
		else frame.add("notDecorated", element.name$());
		FrameBuilder abstractBoxFrame = new FrameBuilder().add("box");
		if (isAbstract) abstractBoxFrame.add("decorated");
		abstractBoxFrame.add("box", boxName());
		frame.add("abstractBox", abstractBoxFrame);
	}

	protected FrameBuilder componentFrame(Component component) {
		ComponentRenderer renderer = factory.renderer(settings, component, templateProvider, target);
		renderer.buildChildren(true);
		renderer.decorated(ElementHelper.isRoot(element));
		renderer.owner(element);
		return renderer.buildFrame();
	}

	protected void addComponent(Component component, FrameBuilder builder) {
		builder.add("component", componentFrame(component));
	}

	private void writeDisplaysFor(AccessibleDisplay display, FrameBuilder builder) {
		write(builder);
		writeNotifier(display.a$(PassiveView.class), builder);
		writeRequester(display.a$(PassiveView.class), builder);
	}

	private void addParent(Display display, FrameBuilder builder) {
		String parent = parent();
		final FrameBuilder parentFrame = new FrameBuilder().add("value", display.parentDisplay()).add("dsl", parent).add("package", parent.substring(0, parent.lastIndexOf(".")));
		builder.add("parent", parentFrame.toFrame());
	}

}