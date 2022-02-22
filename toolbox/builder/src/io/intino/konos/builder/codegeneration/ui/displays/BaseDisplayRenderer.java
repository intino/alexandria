package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRendererFactory;
import io.intino.konos.builder.codegeneration.ui.passiveview.PassiveViewRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.InteractionComponents.Actionable;
import io.intino.konos.model.graph.OtherComponents.Dialog;
import io.intino.konos.model.graph.OtherComponents.OwnerTemplateStamp;
import io.intino.konos.model.graph.OtherComponents.ProxyStamp;
import io.intino.konos.model.graph.OtherComponents.TemplateStamp;
import io.intino.magritte.framework.Layer;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.model.graph.CatalogComponents.Collection;
import static io.intino.konos.model.graph.Component.DynamicLoaded;
import static io.intino.konos.model.graph.OtherComponents.Selector;

public abstract class BaseDisplayRenderer<D extends Display> extends PassiveViewRenderer<D> {
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();

	protected BaseDisplayRenderer(CompilationContext context, D display, TemplateProvider templateProvider, Target target) {
		super(context, display, templateProvider, target);
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
		if (!hasAbstractClass(element)) result.add("noAbstract");
		addParametrized(result);
		addExtends(result);
		addImports(result, accessible);
		addImplements(result);
		addMethods(result);
		addRenderTagFrames(result);
		addDecoratedFrames(result);
		result.add("notifier", notifierName(element));
		result.add("requester", requesterName(element));
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
		result.add("notifier", element.i$(Template.class) ? "Template" : element.name$());
		addGeneric(element, result);
		addDecoratedFrames(result);
		frame.add("parametrized", result.toFrame());
	}

	private void addExtends(FrameBuilder frame) {
		FrameBuilder result = new FrameBuilder("displayExtends");
		if (element.i$(Dialog.class)) result.add(Dialog.class.getSimpleName());
		if (element.i$(Template.class)) result.add(Template.class.getSimpleName());
		if (element.i$(CatalogComponents.List.class) || element.i$(CatalogComponents.Magazine.class) ||
			element.i$(CatalogComponents.Table.class) || element.i$(CatalogComponents.Map.class) ||
			element.i$(CatalogComponents.DynamicTable.class))
			result.add("collection");
		if (element.i$(CatalogComponents.Table.class)) result.add(CatalogComponents.Table.class.getSimpleName());
		if (element.i$(CatalogComponents.DynamicTable.class)) result.add(CatalogComponents.DynamicTable.class.getSimpleName());
		if (element.i$(Collection.Mold.Item.class)) result.add(Collection.Mold.Item.class.getSimpleName());
		if (element.i$(HelperComponents.Row.class)) result.add(HelperComponents.Row.class.getSimpleName());
		addGeneric(element, result);
		result.add("type", typeOf(element));
		addDecoratedFrames(result);
		if (element.i$(Template.class)) {
			String modelClass = element.a$(Template.class).modelClass();
			result.add("modelClass", modelClass != null ? modelClass : "java.lang.Void");
		}
		if (element.i$(Collection.class)) {
			Collection.Mold mold = element.a$(Collection.class).mold(0);
			String itemClass = element.a$(Collection.class).itemClass();
			result.add("componentType", firstUpperCase(nameOf(mold.item())));
			result.add("itemClass", itemClass != null ? itemClass : "java.lang.Void");
		}
		if (element.i$(Collection.Mold.Item.class)) {
			String itemClass = element.a$(Collection.Mold.Item.class).core$().ownerAs(Collection.class).itemClass();
			result.add("itemClass", itemClass != null ? itemClass : "java.lang.Void");
		}
		if (element.i$(HelperComponents.Row.class)) {
			String itemClass = element.a$(HelperComponents.Row.class).items(0).core$().ownerAs(Collection.class).itemClass();
			result.add("itemClass", itemClass != null ? itemClass : "java.lang.Void");
		}
		result.add("name", nameOf(element));
		frame.add("displayExtends", result);
	}

	private void addImports(FrameBuilder frame, boolean accessible) {
		KonosGraph graph = element.graph();
		if (graph.templateList().size() > 0) frame.add("templatesImport", buildBaseFrame().add("templatesImport"));
		if (graph.blockList().size() > 0) frame.add("blocksImport", buildBaseFrame().add("blocksImport"));
		if (graph.itemsDisplays(context.graphName()).size() > 0) frame.add("itemsImport", buildBaseFrame().add("itemsImport"));
		if (graph.rowsDisplays(context.graphName()).size() > 0) frame.add("rowsImport", buildBaseFrame().add("rowsImport"));
		if (graph.tablesDisplays(context.graphName()).size() > 0) frame.add("tablesImport", buildBaseFrame().add("tablesImport"));
		if (graph.listsDisplays(context.graphName()).size() > 0) frame.add("listsImport", buildBaseFrame().add("listsImport"));
		if (graph.magazinesDisplays(context.graphName()).size() > 0) frame.add("magazinesImport", buildBaseFrame().add("magazinesImport"));
		if (graph.mapsDisplays(context.graphName()).size() > 0) frame.add("mapsImport", buildBaseFrame().add("mapsImport"));
		if (graph.dynamicTablesDisplays(context.graphName()).size() > 0) frame.add("dynamicTablesImport", buildBaseFrame().add("dynamicTablesImport"));
		frame.add("notifierImport", notifierImportFrame(element));
		if (!ElementHelper.isRoot(componentOf(element)) || (element.isAccessible() && accessible))
			frame.add("displayRegistration", displayRegistrationFrame(accessible));
		frame.add("requesterDirectory", typeOf(element).equalsIgnoreCase("Display") || typeOf(element).equalsIgnoreCase("Display") ? "." : "..");
		frame.add("notifierDirectory", typeOf(element).equalsIgnoreCase("Display") ? "." : "..");
	}

	private FrameBuilder displayRegistrationFrame(boolean accessible) {
		FrameBuilder result = buildBaseFrame().add("displayRegistration");
		if (element.isAccessible() && accessible) result.add("accessible");
		return result.add("name", nameOf(element));
	}

	protected void addImplements(FrameBuilder frame) {
		if (element.i$(DynamicLoaded.class))
			frame.add("implements", new FrameBuilder("implements", DynamicLoaded.class.getSimpleName()));
		if (element.i$(Collection.Selectable.class))
			frame.add("implements", new FrameBuilder("implements", Collection.Selectable.class.getSimpleName()));
		if (element.i$(Multiple.class)) {
			FrameBuilder result = multipleImplements();
			if (element.a$(Multiple.class).collapsed()) result.add("collapsable");
			frame.add("implements", result);
		}
		if ((element.i$(Actionable.Action.class) || (element.i$(Actionable.OpenLayer.class))) && element.i$(Actionable.Addressable.class))
			frame.add("implements", new FrameBuilder("implements", Actionable.Action.class.getSimpleName(), Actionable.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(Selector.Addressable.class))
			frame.add("implements", new FrameBuilder("implements", Selector.class.getSimpleName(), Selector.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(CatalogComponents.SearchBox.Addressable.class))
			frame.add("implements", new FrameBuilder("implements", CatalogComponents.SearchBox.class.getSimpleName(), CatalogComponents.SearchBox.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(CatalogComponents.Grouping.Addressable.class))
			frame.add("implements", new FrameBuilder("implements", CatalogComponents.Grouping.class.getSimpleName(), CatalogComponents.Grouping.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(CatalogComponents.Sorting.Addressable.class))
			frame.add("implements", new FrameBuilder("implements", CatalogComponents.Sorting.class.getSimpleName(), CatalogComponents.Sorting.Addressable.class.getSimpleName()).add("name", nameOf(element)));
	}

	private FrameBuilder multipleImplements() {
		FrameBuilder result = new FrameBuilder("implements", Multiple.class.getSimpleName());
		addDecoratedFrames(result);
		String objectType = multipleObjectType(element);
		result.add("componentType", multipleComponentType(element));
		result.add("componentName", multipleComponentName(element));
		if (!isMultipleSpecificComponent(element) && element.i$(Editable.class)) result.add("componentPrefix", nameOf(element));
		if (objectType != null) result.add("objectType", objectType);
		return result;
	}

	protected String multipleObjectType(Layer element) {
		if (element.i$(DataComponents.Text.Multiple.class)) return "java.lang.String";
		if (element.i$(DataComponents.File.Multiple.class)) return "io.intino.alexandria.ui.File";
		if (element.i$(DataComponents.Image.Multiple.class)) return "io.intino.alexandria.ui.File";
		if (element.i$(OtherComponents.Icon.Multiple.class)) return "java.net.URL";
		if (element.i$(DataComponents.Number.Multiple.class)) return "java.lang.Double";
		if (element.i$(DataComponents.Date.Multiple.class)) return "java.time.Instant";
		if (element.i$(OtherComponents.BaseStamp.Multiple.class)) {
			String modelClass = element.i$(OwnerTemplateStamp.class) ? "java.lang.Void" : element.a$(TemplateStamp.class).template().modelClass();
			return modelClass != null ? modelClass : "java.lang.Void";
		}
		if (element.i$(Block.Multiple.class)) return "java.lang.Void";
		return null;
	}

	protected String multipleComponentType(Layer element) {
		String prefix = "io.intino.alexandria.ui.displays.components.";
		String name = multipleComponentName(element);
		if (name == null) return null;
		return element.i$(OtherComponents.BaseStamp.Multiple.class) || element.i$(Block.Multiple.class) ? name : prefix + name;
	}

	protected String multipleComponentName(Layer element) {
		String editable = element.i$(Editable.class) ? "Editable" : "";
		if (element.i$(DataComponents.Text.Multiple.class)) return "Text" + editable;
		if (element.i$(DataComponents.File.Multiple.class)) return "File" + editable;
		if (element.i$(DataComponents.Image.Multiple.class)) return "Image" + editable;
		if (element.i$(OtherComponents.Icon.Multiple.class)) return "Icon" + editable;
		if (element.i$(DataComponents.Number.Multiple.class)) return "Number" + editable;
		if (element.i$(DataComponents.Date.Multiple.class)) return "Date" + editable;
		if (element.i$(OtherComponents.BaseStamp.Multiple.class)) {
			if (element.i$(OtherComponents.OwnerTemplateStamp.class)) {
				OwnerTemplateStamp stamp = element.a$(OwnerTemplateStamp.class);
				return ownerTemplateStampPackage(stamp.owner()) + "." + firstUpperCase(stamp.template());
			}
			return firstUpperCase(element.a$(TemplateStamp.class).template().name$());
		}
		if (element.i$(Block.Multiple.class)) return firstUpperCase(nameOf(element));
		return null;
	}

	protected boolean isMultipleSpecificComponent(Layer element) {
		return element.i$(OtherComponents.BaseStamp.Multiple.class) || element.i$(Block.Multiple.class);
	}

	protected String ownerTemplateStampPackage(Service.UI.Use use) {
		return use.package$() + ".box.ui.displays.templates";
	}

	protected void addMethods(FrameBuilder frame) {
		if (element.i$(Component.DynamicLoaded.class)) {
			frame.add("baseMethod", "renderDynamicLoaded");
			frame.add("methods", new FrameBuilder("methods", Component.DynamicLoaded.class.getSimpleName()).add("loadTime", element.a$(Component.DynamicLoaded.class).loadTime().name()));
		} else if (element.i$(Dialog.class)) {
			frame.add("baseMethod", "renderDialog");
		} else if (element.i$(HelperComponents.Row.class)) {
			frame.add("baseMethod", "renderRow");
		}
	}

	protected void addRenderTagFrames(FrameBuilder frame) {
		FrameBuilder renderTag = new FrameBuilder("renderTag");
		if (element.i$(Block.class)) {
			ComponentRenderer renderer = factory.renderer(context, element.a$(Block.class), templateProvider, target);
			renderTag.add(Block.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		} else if (element.i$(Template.class)) {
			ComponentRenderer renderer = factory.renderer(context, element.a$(Template.class), templateProvider, target);
			renderTag.add(Template.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		} else if (element.i$(HelperComponents.Row.class)) {
			ComponentRenderer renderer = factory.renderer(context, element.a$(HelperComponents.Row.class), templateProvider, target);
			renderTag.add(HelperComponents.Row.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		} else if (element.i$(Collection.Mold.Item.class)) {
			renderTag.add(Collection.Mold.Item.class.getSimpleName());
		}
		frame.add("renderTag", renderTag);
	}

	protected void addDecoratedFrames(FrameBuilder frame) {
		addDecoratedFrames(frame, ElementHelper.isRoot(element));
	}

	protected void addDecoratedFrames(FrameBuilder frame, boolean decorated) {
		boolean isAbstract = decorated && !(element.i$(TemplateStamp.class) || element.i$(OwnerTemplateStamp.class) || element.i$(ProxyStamp.class));
		if (isAbstract) frame.add("abstract", "Abstract");
		else frame.add("notDecorated", element.name$());
		FrameBuilder abstractBoxFrame = new FrameBuilder().add("box");
		if (isAbstract) abstractBoxFrame.add("decorated");
		abstractBoxFrame.add("box", boxName());
		if (belongsToAccessible(element)) abstractBoxFrame.add("accessible");
		frame.add("abstractBox", abstractBoxFrame);
	}

	protected FrameBuilder componentFrame(Component component, Display virtualParent) {
		ComponentRenderer renderer = factory.renderer(context, component, templateProvider, target);
		renderer.buildChildren(true);
		renderer.decorated(ElementHelper.isRoot(element));
		renderer.owner(element);
		renderer.virtualParent(virtualParent);
		return renderer.buildFrame();
	}

	protected void addComponent(Component component, Display virtualParent, FrameBuilder builder) {
		builder.add("component", componentFrame(component, virtualParent));
	}

	protected boolean belongsToAccessible(Display element) {
		if (element.isAccessible()) return true;
		Display owner = element.core$().ownerAs(Display.class);
		while (owner != null) {
			if (owner.isAccessible()) return true;
			owner = owner.core$().ownerAs(Display.class);
		}
		return false;
	}

	private void writeDisplaysFor(Display.Accessible display, FrameBuilder builder) {
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