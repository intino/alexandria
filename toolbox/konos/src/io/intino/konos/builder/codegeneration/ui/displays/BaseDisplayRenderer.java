package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRendererFactory;
import io.intino.konos.builder.codegeneration.ui.passiveview.PassiveViewRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.dsl.ActionableComponents.Actionable;
import io.intino.konos.dsl.*;
import io.intino.konos.dsl.OtherComponents.Dialog;
import io.intino.konos.dsl.OtherComponents.OwnerTemplateStamp;
import io.intino.konos.dsl.OtherComponents.ProxyStamp;
import io.intino.konos.dsl.OtherComponents.TemplateStamp;
import io.intino.konos.dsl.rules.Layout;
import io.intino.magritte.framework.Layer;

import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.hasAbstractClass;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.isScrollable;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;
import static io.intino.konos.dsl.CatalogComponents.Collection;
import static io.intino.konos.dsl.Component.DynamicLoaded;
import static io.intino.konos.dsl.OtherComponents.Selector;

public abstract class BaseDisplayRenderer<D extends Display> extends PassiveViewRenderer<D> {
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();

	protected BaseDisplayRenderer(CompilationContext context, D display, RendererWriter rendererWriter) {
		super(context, display, rendererWriter);
	}

	@Override
	public void render() {
		if (element == null) return;
		String path = path(element, writer.target());
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
		if (!hasAbstractClass(element, writer.target())) result.add("noAbstract");
		if (isScrollable(element, writer.target())) result.add("scrollable");
		addParametrized(result, accessible);
		addExtends(result, accessible);
		addImports(result, accessible);
		addImplements(result);
		addMethods(result);
		addRenderTagFrames(result);
		addDecoratedFrames(result);
		addAccessibleFrames(result, accessible);
		result.add("notifier", notifierName(element));
		result.add("requester", requesterName(element));
		result.add("componentType", element.components().stream().map(this::typeOf).distinct().map(type -> new FrameBuilder().add("componentType", type).toFrame()).toArray(Frame[]::new));
		result.add("layout", layoutFrame());
		if (element.parentDisplay() != null) addParent(element, result);
		if (!element.graph().schemaList().isEmpty())
			result.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()));
		if (element.isAccessible())
			result.add("parameter", element.asAccessible().parameters().stream().map(p -> new FrameBuilder("parameter", "accessible").add("value", p).toFrame()).toArray(Frame[]::new));
		return result;
	}

	private FrameBuilder layoutFrame() {
		if (!element.i$(Template.class)) return new FrameBuilder("layout", "vertical");
		FrameBuilder result = new FrameBuilder("layout");
		List<Layout> layout = element.a$(Template.class).layout();
		result.add(layout.contains(Layout.Horizontal) ? "horizontal" : "vertical");
		return result;
	}

	private void addParametrized(FrameBuilder frame, boolean accessible) {
		FrameBuilder result = new FrameBuilder("parametrized");
		result.add("name", element.name$());
		result.add("notifier", element.i$(conceptOf(Template.class)) ? "Template" : element.name$());
		addGeneric(element, result);
		if (!accessible && element.isAccessible()) result.add("accessible");
		addDecoratedFrames(result);
		frame.add("parametrized", result.toFrame());
	}

	private void addExtends(FrameBuilder frame, boolean accessible) {
		FrameBuilder result = buildBaseFrame().add("displayExtends");
		if (element.i$(conceptOf(Dialog.class))) result.add(Dialog.class.getSimpleName());
		if (element.i$(conceptOf(Template.class))) result.add(Template.class.getSimpleName());
		if (element.i$(conceptOf(CatalogComponents.List.class)) || element.i$(conceptOf(CatalogComponents.Magazine.class)) ||
				element.i$(conceptOf(CatalogComponents.Table.class)) || element.i$(conceptOf(CatalogComponents.Map.class)) ||
				element.i$(conceptOf(CatalogComponents.DynamicTable.class)))
			result.add("collection");
		if (element.i$(conceptOf(CatalogComponents.Magazine.class)))
			result.add(CatalogComponents.Magazine.class.getSimpleName());
		if (element.i$(conceptOf(CatalogComponents.Table.class)))
			result.add(CatalogComponents.Table.class.getSimpleName());
		if (element.i$(conceptOf(CatalogComponents.DynamicTable.class)))
			result.add(CatalogComponents.DynamicTable.class.getSimpleName());
		if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)))
			result.add(CatalogComponents.Moldable.Mold.Item.class.getSimpleName());
		if (element.i$(conceptOf(HelperComponents.Row.class))) result.add(HelperComponents.Row.class.getSimpleName());
		if (!accessible && element.isAccessible()) result.add("accessible");
		addGeneric(element, result);
		result.add("type", typeOf(element));
		addDecoratedFrames(result);
		if (element.i$(conceptOf(Template.class))) {
			String modelClass = element.a$(Template.class).modelClass();
			result.add("modelClass", modelClass != null ? modelClass : "java.lang.Void");
		}
		if (element.i$(conceptOf(CatalogComponents.Moldable.class))) {
			CatalogComponents.Moldable.Mold mold = element.a$(CatalogComponents.Moldable.class).mold(0);
			String itemClass = element.a$(Collection.class).itemClass();
			result.add("componentType", firstUpperCase(nameOf(mold.item())));
			result.add("itemClass", itemClass != null ? itemClass : "java.lang.Void");
		}
		if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) {
			String itemClass = element.a$(CatalogComponents.Moldable.Mold.Item.class).core$().ownerAs(Collection.class).itemClass();
			result.add("itemClass", itemClass != null ? itemClass : "java.lang.Void");
		}
		if (element.i$(conceptOf(HelperComponents.Row.class))) {
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
		if (graph.itemsDisplays(context.graphName()).size() > 0)
			frame.add("itemsImport", buildBaseFrame().add("itemsImport"));
		if (graph.rowsDisplays(context.graphName()).size() > 0)
			frame.add("rowsImport", buildBaseFrame().add("rowsImport"));
		if (graph.tablesDisplays(context.graphName()).size() > 0)
			frame.add("tablesImport", buildBaseFrame().add("tablesImport"));
		if (graph.listsDisplays(context.graphName()).size() > 0)
			frame.add("listsImport", buildBaseFrame().add("listsImport"));
		if (graph.magazinesDisplays(context.graphName()).size() > 0)
			frame.add("magazinesImport", buildBaseFrame().add("magazinesImport"));
		if (graph.mapsDisplays(context.graphName()).size() > 0)
			frame.add("mapsImport", buildBaseFrame().add("mapsImport"));
		if (graph.dynamicTablesDisplays(context.graphName()).size() > 0)
			frame.add("dynamicTablesImport", buildBaseFrame().add("dynamicTablesImport"));
		frame.add("notifierImport", notifierImportFrame(element, accessible));
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
		if (element.i$(conceptOf(DynamicLoaded.class)))
			frame.add("implements", new FrameBuilder("implements", DynamicLoaded.class.getSimpleName()));
		if (element.i$(conceptOf(Collection.Selectable.class)))
			frame.add("implements", new FrameBuilder("implements", Collection.Selectable.class.getSimpleName()));
		if (element.i$(conceptOf(Multiple.class))) {
			FrameBuilder result = multipleImplements();
			if (element.a$(Multiple.class).collapsed()) result.add("collapsable");
			frame.add("implements", result);
		}
		if ((element.i$(conceptOf(Actionable.Action.class)) || (element.i$(conceptOf(Actionable.OpenLayer.class)))) && element.i$(conceptOf(Actionable.Addressable.class)))
			frame.add("implements", new FrameBuilder("implements", Actionable.Action.class.getSimpleName(), Actionable.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(conceptOf(Selector.Addressable.class)))
			frame.add("implements", new FrameBuilder("implements", Selector.class.getSimpleName(), Selector.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(conceptOf(CatalogComponents.SearchBox.Addressable.class)))
			frame.add("implements", new FrameBuilder("implements", CatalogComponents.SearchBox.class.getSimpleName(), CatalogComponents.SearchBox.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(conceptOf(CatalogComponents.Grouping.Addressable.class)))
			frame.add("implements", new FrameBuilder("implements", CatalogComponents.Grouping.class.getSimpleName(), CatalogComponents.Grouping.Addressable.class.getSimpleName()).add("name", nameOf(element)));
		if (element.i$(conceptOf(CatalogComponents.Sorting.Addressable.class)))
			frame.add("implements", new FrameBuilder("implements", CatalogComponents.Sorting.class.getSimpleName(), CatalogComponents.Sorting.Addressable.class.getSimpleName()).add("name", nameOf(element)));
	}

	private FrameBuilder multipleImplements() {
		FrameBuilder result = new FrameBuilder("implements", Multiple.class.getSimpleName());
		addDecoratedFrames(result);
		String objectType = multipleObjectType(element);
		result.add("componentType", multipleComponentType(element));
		result.add("componentName", multipleComponentName(element));
		if (!isMultipleSpecificComponent(element) && element.i$(conceptOf(Editable.class)))
			result.add("componentPrefix", nameOf(element));
		if (objectType != null) result.add("objectType", objectType);
		return result;
	}

	protected String multipleObjectType(Layer element) {
		if (element.i$(conceptOf(DataComponents.Text.Multiple.class))) return "java.lang.String";
		if (element.i$(conceptOf(DataComponents.File.Multiple.class))) return "io.intino.alexandria.ui.File";
		if (element.i$(conceptOf(DataComponents.Image.Multiple.class))) return "io.intino.alexandria.ui.File";
		if (element.i$(conceptOf(OtherComponents.Icon.Multiple.class))) return "java.net.URL";
		if (element.i$(conceptOf(DataComponents.Number.Multiple.class))) return "java.lang.Double";
		if (element.i$(conceptOf(DataComponents.Date.Multiple.class)))
			return writer.target() == Target.Android ? "kotlinx.datetime.Instant" : "java.time.Instant";
		if (element.i$(conceptOf(OtherComponents.BaseStamp.Multiple.class))) {
			String modelClass = null;
			if (element.i$(conceptOf(OwnerTemplateStamp.class))) modelClass = "java.lang.Void";
			else if (element.i$(conceptOf(TemplateStamp.class)))
				modelClass = element.a$(TemplateStamp.class).template() != null ? element.a$(TemplateStamp.class).template().modelClass() : null;
			return modelClass != null ? modelClass : "java.lang.Void";
		}
		if (element.i$(conceptOf(Block.Multiple.class))) return "java.lang.Void";
		return null;
	}

	protected String multipleComponentType(Layer element) {
		String prefix = "io.intino.alexandria." + targetPackageName() + ".displays.components.";
		String name = multipleComponentName(element);
		if (name == null) return null;
		return element.i$(conceptOf(OtherComponents.BaseStamp.Multiple.class)) || element.i$(conceptOf(Block.Multiple.class)) ? name : prefix + name;
	}

	protected String multipleComponentName(Layer element) {
		String editable = element.i$(conceptOf(Editable.class)) ? "Editable" : "";
		if (element.i$(conceptOf(DataComponents.Text.Multiple.class))) return "Text" + editable;
		if (element.i$(conceptOf(DataComponents.File.Multiple.class))) return "File" + editable;
		if (element.i$(conceptOf(DataComponents.Image.Multiple.class))) return "Image" + editable;
		if (element.i$(conceptOf(OtherComponents.Icon.Multiple.class))) return "Icon" + editable;
		if (element.i$(conceptOf(DataComponents.Number.Multiple.class))) return "Number" + editable;
		if (element.i$(conceptOf(DataComponents.Date.Multiple.class))) return "Date" + editable;
		if (element.i$(conceptOf(OtherComponents.BaseStamp.Multiple.class))) {
			if (element.i$(conceptOf(OtherComponents.OwnerTemplateStamp.class))) {
				OwnerTemplateStamp stamp = element.a$(OwnerTemplateStamp.class);
				return ownerTemplateStampPackage(stamp.owner()) + "." + firstUpperCase(stamp.template());
			}
			return element.i$(conceptOf(TemplateStamp.class)) ? firstUpperCase(element.a$(TemplateStamp.class).template().name$()) : "io.intino.alexandria." + targetPackageName() + ".Display";
		}
		if (element.i$(conceptOf(Block.Multiple.class))) return firstUpperCase(nameOf(element));
		return null;
	}

	protected boolean isMultipleSpecificComponent(Layer element) {
		return element.i$(conceptOf(OtherComponents.BaseStamp.Multiple.class)) || element.i$(conceptOf(Block.Multiple.class));
	}

	protected String ownerTemplateStampPackage(Service.UI.Use use) {
		return use.package$() + ".box.ui.displays.templates";
	}

	protected void addMethods(FrameBuilder frame) {
		if (element.i$(conceptOf(Component.DynamicLoaded.class))) {
			frame.add("baseMethod", "renderDynamicLoaded");
			frame.add("methods", new FrameBuilder("methods", Component.DynamicLoaded.class.getSimpleName()).add("loadTime", element.a$(Component.DynamicLoaded.class).loadTime().name()));
		} else if (element.i$(conceptOf(Dialog.class))) {
			frame.add("baseMethod", "renderDialog");
		} else if (element.i$(conceptOf(HelperComponents.Row.class))) {
			frame.add("baseMethod", "renderRow");
		}
	}

	protected void addRenderTagFrames(FrameBuilder frame) {
		FrameBuilder renderTag = new FrameBuilder("renderTag");
		if (element.i$(conceptOf(Block.class))) {
			ComponentRenderer renderer = factory.renderer(context, element.a$(Block.class), writer);
			renderTag.add(Block.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		} else if (element.i$(conceptOf(Template.class))) {
			ComponentRenderer renderer = factory.renderer(context, element.a$(Template.class), writer);
			renderTag.add(Template.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		} else if (element.i$(conceptOf(HelperComponents.Row.class))) {
			ComponentRenderer renderer = factory.renderer(context, element.a$(HelperComponents.Row.class), writer);
			renderTag.add(HelperComponents.Row.class.getSimpleName());
			renderTag.add("properties", renderer.properties());
		} else if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) {
			renderTag.add(CatalogComponents.Moldable.Mold.Item.class.getSimpleName());
		}
		frame.add("renderTag", renderTag);
	}

	protected void addDecoratedFrames(FrameBuilder frame) {
		addDecoratedFrames(frame, ElementHelper.isRoot(element));
	}

	protected void addDecoratedFrames(FrameBuilder frame, boolean decorated) {
		boolean isAbstract = decorated && !(element.i$(conceptOf(TemplateStamp.class)) || element.i$(conceptOf(OwnerTemplateStamp.class)) || element.i$(conceptOf(ProxyStamp.class)));
		if (isAbstract) frame.add("abstract", "Abstract");
		else frame.add("notDecorated", element.name$());
		FrameBuilder abstractBoxFrame = new FrameBuilder().add("box");
		if (isAbstract) abstractBoxFrame.add("decorated");
		abstractBoxFrame.add("box", boxName());
		if (belongsToAccessible(element)) abstractBoxFrame.add("accessible");
		frame.add("abstractBox", abstractBoxFrame);
	}

	protected void addAccessibleFrames(FrameBuilder frame, boolean accessibleDisplay) {
		FrameBuilder frameBuilder = accessibleNotifierImportFrame();
		if (accessibleDisplay) {
			frame.add("accessibleNotifierImport", frameBuilder);
			return;
		}
		boolean accessible = element.isAccessible();
		if (!accessible) {
			frame.add("accessibleNotifierImport", frameBuilder);
			return;
		}
		frame.add("accessibleNotifier", buildBaseFrame().add("name", nameOf(element)));
		frame.add("accessibleNotifierImport", frameBuilder.add("accessible"));
		frame.add("notifyProxyMethod", buildBaseFrame().add("notifyProxyMethod"));
	}

	private FrameBuilder accessibleNotifierImportFrame() {
		FrameBuilder frameBuilder = buildBaseFrame().add("accessibleNotifierImport").add("name", nameOf(element));
		frameBuilder.add("notifier", notifierName(element));
		if (context.serviceDirectory() != null && context.serviceDirectory().exists())
			frameBuilder.add("serviceName", context.serviceDirectory().getName());
		return frameBuilder;
	}

	protected FrameBuilder componentFrame(Component component, Display virtualParent) {
		ComponentRenderer renderer = factory.renderer(context, component, writer);
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
		writer.writeNotifier(display.a$(PassiveView.class), builder);
		writer.writeRequester(display.a$(PassiveView.class), builder);
	}

	private void addParent(Display display, FrameBuilder builder) {
		String parent = parent();
		final FrameBuilder parentFrame = new FrameBuilder().add("value", display.parentDisplay()).add("dsl", parent).add("package", parent.substring(0, parent.lastIndexOf(".")));
		builder.add("parent", parentFrame.toFrame());
	}

}