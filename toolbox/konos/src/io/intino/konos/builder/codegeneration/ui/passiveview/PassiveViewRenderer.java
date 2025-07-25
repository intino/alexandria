package io.intino.konos.builder.codegeneration.ui.passiveview;

import cottons.utils.StringHelper;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.ElementRenderer;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.dsl.*;
import io.intino.konos.dsl.ActionableComponents.IconToggle;
import io.intino.konos.dsl.ActionableComponents.MaterialIconToggle;
import io.intino.konos.dsl.ActionableComponents.Switch;
import io.intino.konos.dsl.ActionableComponents.Toggle;
import io.intino.konos.dsl.OtherComponents.*;
import io.intino.konos.dsl.PassiveView.Notification;
import io.intino.konos.dsl.PassiveView.Request;
import io.intino.konos.dsl.VisualizationComponents.Dashboard;
import io.intino.konos.dsl.VisualizationComponents.DocumentEditor;
import io.intino.magritte.framework.Layer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.hasAbstractClass;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;
import static io.intino.konos.dsl.OtherComponents.*;
import static io.intino.konos.dsl.PassiveView.Request.ResponseType.Asset;
import static java.util.stream.Collectors.toList;

public abstract class PassiveViewRenderer<C extends PassiveView> extends ElementRenderer<C> {
	private Display virtualParent;

	public static final String ProjectComponentImport = "projectComponentImport";
	public static final String AlexandriaComponentImport = "alexandriaComponentImport";

	protected PassiveViewRenderer(CompilationContext compilationContext, C element, RendererWriter rendererWriter) {
		super(compilationContext, element, rendererWriter);
	}

	public Display virtualParent() {
		return virtualParent;
	}

	public void virtualParent(Display parent) {
		this.virtualParent = parent;
	}

	@Override
	public FrameBuilder buildFrame() {
		return buildFrame(false);
	}

	public FrameBuilder buildFrame(boolean exposed) {
		FrameBuilder result = super.buildFrame();
		if (exposed) result.add("exposed");
		if (element.i$(conceptOf(CatalogComponents.Collection.class))) result.add("collection");
		FrameBuilder extensionFrame = extensionFrame(exposed);
		String type = type();
		result.add("id", shortId(element, virtualParent != null ? virtualParent.name$() : ""));
		result.add("type", type);
		addParentImport(result);
		result.add("parentType", extensionFrame);
		result.add("import", extensionFrame);
		result.add("proxy", extensionFrame);
		if (!type.equalsIgnoreCase("display")) result.add("packageType", type.toLowerCase());
		result.add("packageTypeRelativeDirectory", packageTypeRelativeDirectory(element));
		result.add("componentDirectory", componentDirectoryOf(element, false));
		result.add("name", nameOf(element));
		result.add("notification", framesOfNotifications(element.notificationList()));
		result.add("event", framesOfEvents(element));
		result.add("request", framesOfRequests(element.requestList()));
		return result;
	}

	protected void createPassiveViewFiles(FrameBuilder elementBuilder) {
		writer.writeRequester(element, elementBuilder);
		writer.writePushRequester(element, elementBuilder);
		writer.writeNotifier(element, elementBuilder);
	}

	protected String type() {
		return typeOf(element.a$(Display.class));
	}

	protected String nameOfPassiveViewFile(PassiveView element, Frame frame, String suffix) {
		return nameOfPassiveViewFile(element, isExposed(frame), suffix);
	}

	protected String nameOfPassiveViewFile(PassiveView element, boolean exposed, String suffix) {
		return snakeCaseToCamelCase(element.name$() + (exposed ? "Proxy" : "") + suffix);
	}

	protected void addGeneric(PassiveView element, FrameBuilder builder) {
		if (!isGeneric(element)) return;
		builder.add("generic");
		if (element.isExtensionOf()) builder.add("isExtensionOf");
		builder.add("parent", genericParent(element));
		builder.add("parentMobileShared", genericParent(element, Target.MobileShared));
	}

	protected boolean isGeneric(PassiveView element) {
		return element.isExtensionOf() || KonosGraph.isParent(context.graphName(), element);
	}

	protected String genericParent(PassiveView element) {
		return genericParent(element, writer.target());
	}

	protected String genericParent(PassiveView element, Target target) {
		if (element.isExtensionOf()) return firstUpperCase(element.asExtensionOf().parentView().name$());
		return (writer.target().requirePackageName() ? "io.intino.alexandria." + targetPackageName(target) + ".displays." : "") + firstUpperCase(typeOf(element));
	}

	protected String packageTypeRelativeDirectory(PassiveView passiveView) {
		return typeOf(passiveView).equalsIgnoreCase("display") ? "" : "../";
	}

	protected void addComponentsImports(FrameBuilder builder) {
		if (element.i$(conceptOf(HelperComponents.Row.class)))
			addComponentsImports(element.a$(HelperComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(toList()), builder);
		else addComponentsImports(components(element), builder);
	}

	protected void addComponentsImports(List<Component> componentList, FrameBuilder builder) {
		HashSet<String> imported = new HashSet<>();
		addComponentsImports(imported, componentList, builder);
		if (!imported.contains("Block") && element.i$(conceptOf(io.intino.konos.dsl.Template.class)))
			builder.add("alexandriaBlockImport", new FrameBuilder("alexandriaImport").add("name", "Block"));
	}

	protected void addFacets(PassiveView passiveView, FrameBuilder builder) {
		List<String> facets = facets(passiveView);
		facets.forEach(facet -> builder.add("facet", facet));
	}

	private List<String> facets(PassiveView passiveView) {
		List<String> result = new ArrayList<>();
		if (passiveView.i$(conceptOf(Switch.class))) result.add("Switch");
		if (passiveView.i$(conceptOf(ActionableComponents.AbstractSplitButton.class))) result.add("Split");
		if (passiveView.i$(conceptOf(Toggle.class)) || passiveView.i$(conceptOf(IconToggle.class)) || passiveView.i$(conceptOf(MaterialIconToggle.class)))
			result.add("Toggle");
		if (passiveView.i$(conceptOf(Editable.class))) result.add("Editable");
		if (passiveView.i$(conceptOf(DataComponents.Text.Code.class))) result.add("Code");
		if (passiveView.i$(conceptOf(Block.Drawer.class))) result.add("Drawer");
		if (passiveView.i$(conceptOf(Block.Badge.class))) result.add("Badge");
		if (passiveView.i$(conceptOf(Block.Conditional.class))) result.add("Conditional");
		if (passiveView.i$(conceptOf(Block.Popover.class))) result.add("Popover");
		if (passiveView.i$(conceptOf(Block.Splitter.class))) result.add("Splitter");
		if (passiveView.i$(conceptOf(Block.Resizable.class))) result.add("Resizable");
		if (passiveView.i$(conceptOf(Selector.Tabs.class))) result.add("Tabs");
		if (passiveView.i$(conceptOf(Selector.Menu.class))) result.add("Menu");
		if (passiveView.i$(conceptOf(Selector.ToggleBox.class))) result.add("ToggleBox");
		if (passiveView.i$(conceptOf(Selector.ComboBox.class))) result.add("ComboBox");
		if (passiveView.i$(conceptOf(Selector.CollectionBox.class))) result.add("CollectionBox");
		if (passiveView.i$(conceptOf(CatalogComponents.Grouping.ComboBox.class))) result.add("ComboBox");
		if (passiveView.i$(conceptOf(Selector.ListBox.class))) result.add("ListBox");
		if (passiveView.i$(conceptOf(Selector.RadioBox.class))) result.add("RadioBox");
		if (passiveView.i$(conceptOf(Selector.CheckBox.class))) result.add("CheckBox");
		if (passiveView.i$(conceptOf(DataComponents.Image.Avatar.class))) result.add("Avatar");
		if (passiveView.i$(conceptOf(CatalogComponents.Sorting.OrderBy.class))) result.add("OrderBy");
		if (passiveView.i$(conceptOf(Block.Parallax.class))) result.add("Parallax");
		if (passiveView.i$(conceptOf(Dashboard.Shiny.class))) result.add("Shiny");
		if (passiveView.i$(conceptOf(Dashboard.Metabase.class))) result.add("Metabase");
		if (passiveView.i$(conceptOf(DocumentEditor.Collabora.class))) result.add("Collabora");
		return result;
	}

	private FrameBuilder importOf(PassiveView passiveView, String container, boolean multiple) {
		FrameBuilder result = new FrameBuilder(container);
		if (passiveView.i$(conceptOf(InheritTemplateStamp.class))) result.add("inherittemplatestamp");
		result.add("name", importNameOf(passiveView));
		result.add("type", importTypeOf(passiveView, multiple));
		result.add("directory", directoryOf(passiveView));
		String componentDirectory = componentDirectoryOf(passiveView, multiple);
		result.add("componentTarget", (componentDirectory != null && componentDirectory.equals("components")) || hasAbstractClass(passiveView, writer.target()) ? "src" : "gen");
		result.add("componentDirectory", componentDirectory);
		if (passiveView.i$(conceptOf(InheritTemplateStamp.class)))
			result.add("ownerModuleName", StringHelper.camelCaseToSnakeCase(passiveView.a$(InheritTemplateStamp.class).owner().service()));
		if (context.serviceDirectory().exists()) result.add("serviceName", context.serviceDirectory().getName());
		if (!multiple) addFacets(passiveView, result);
		return result;
	}

	private String importNameOf(PassiveView passiveView) {
		if (passiveView.i$(conceptOf(InheritTemplateStamp.class)))
			return passiveView.a$(InheritTemplateStamp.class).template();
		if (passiveView.i$(conceptOf(TemplateStamp.class)))
			return passiveView.a$(TemplateStamp.class).template().name$();
		if (passiveView.i$(conceptOf(DisplayStamp.class))) {
			Display display = passiveView.a$(DisplayStamp.class).display();
			return display != null ? display.name$() : nameOf(passiveView);
		}
		return nameOf(passiveView);
	}

	protected Object importTypeOf(PassiveView passiveView, boolean multiple) {
		if (multiple)
			return passiveView.i$(conceptOf(DataComponents.Image.class)) ? "MultipleImage" : "multiple";
		if (passiveView.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) || passiveView.i$(conceptOf(HelperComponents.Row.class)))
			return passiveView.name$();
		return typeOf(passiveView);
	}

	protected PassiveView componentOf(PassiveView passiveView) {
		PassiveView component = passiveView;
		if (passiveView.i$(conceptOf(TemplateStamp.class)))
			component = passiveView.a$(TemplateStamp.class).template();
		if (passiveView.i$(conceptOf(DisplayStamp.class))) {
			Display display = passiveView.a$(DisplayStamp.class).display();
			component = display != null ? display : passiveView;
		}
		return component;
	}

	private String directoryOf(PassiveView passiveView) {
		PassiveView component = componentOf(passiveView);
		return ElementHelper.isRoot(component) && hasAbstractClass(component, writer.target()) ? "src" : "gen";
	}

	private String componentDirectoryOf(PassiveView passiveView, boolean multiple) {
		if (multiple && passiveView.i$(conceptOf(Multiple.class))) return "components";
		if (passiveView.i$(conceptOf(TemplateStamp.class))) {
			io.intino.konos.dsl.Template template = passiveView.a$(TemplateStamp.class).template();
			return template != null ? componentDirectoryOf(template, multiple) : null;
		}
		if (passiveView.i$(conceptOf(DisplayStamp.class))) {
			Display display = passiveView.a$(DisplayStamp.class).display();
			return display != null ? componentDirectoryOf(display, multiple) : null;
		}
		if (passiveView.i$(conceptOf(io.intino.konos.dsl.Template.class))) return "templates";
		if (passiveView.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) return "items";
		if (passiveView.i$(conceptOf(HelperComponents.Row.class))) return "rows";
		if (passiveView.i$(conceptOf(Component.class))) return "components";
		return null;
	}

	private boolean isExposed(Frame frame) {
		return frame.is("exposed");
	}

	private FrameBuilder extensionFrame(boolean exposed) {
		String type = type();
		FrameBuilder result = new FrameBuilder().add(type, "").add("value", type).add("type", type);
		if (element.isExtensionOf()) {
			result.add("extensionOf", "extensionOf");
			result.add("parent", element.asExtensionOf().parentView().name$());
		}
		if (type.equalsIgnoreCase("Component")) result.add("component", "component");
		else if (exposed && element.i$(conceptOf(Display.Exposed.class))) result.add("exposed", "exposed");
		else if (isBaseType(element) && !type.equalsIgnoreCase("Display"))
			result.add("baseType", "baseType");
		return result;
	}

	private Frame[] framesOfNotifications(List<Notification> notifications) {
		return notifications.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Notification notification) {
		final FrameBuilder result = new FrameBuilder().add("notification");
		result.add("name", notification.name$());
		result.add("target", notification.to().name());
		if (notification.isType()) {
			final FrameBuilder parameterFrame = buildBaseFrame().add("parameter")
					.add(notification.asType().type())
					.add(notification.asType().getClass().getSimpleName().replace("Data", ""))
					.add("value", notification.asType().type());
			if (notification.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
			if (notification.isFile()) result.add("progressCallback", "progressCallback");
		}
		return result.toFrame();
	}

	private Frame[] framesOfEvents(C element) {
		if (!element.i$(conceptOf(Display.Exposed.class))) return new Frame[0];
		List<String> events = element.a$(Display.Exposed.class).events();
		return events.stream().map(e -> new FrameBuilder("event").add("name", e).toFrame()).toArray(Frame[]::new);
	}

	private Frame[] framesOfRequests(List<Request> requests) {
		return requests.stream().map(r -> frameOf(element, r, packageName())).toArray(Frame[]::new);
	}

	private void addParentImport(FrameBuilder builder) {
		FrameBuilder result = buildBaseFrame().add("value", type());
		if (isGeneric(element) && element.isExtensionOf()) {
			result.add("parent", genericParent(element));
			result.add("parentMobileShared", genericParent(element, Target.MobileShared));
			result.add("parentDirectory", componentDirectoryOf(element.asExtensionOf().parentView(), false));
		} else if (typeOf(element).equalsIgnoreCase("component")) result.add("baseComponent", "");
		else if (builder.is("exposed")) result.add("exposed", "");
		else if (typeOf(element).equalsIgnoreCase("display")) result.add("baseDisplay", "");
		else if (element.i$(conceptOf(Component.class))) {
			if (isEmbeddedComponent(element.a$(Component.class))) result.add("embeddedComponent", "");
			else result.add("component", "");
		} else if (ElementHelper.isRoot(element)) result.add("abstract", "");
		builder.add("parent", result);
	}

	private void addComponentsImports(Set<String> imported, List<Component> componentList, FrameBuilder builder) {
		componentList.forEach(c -> {
			boolean multiple = c.i$(conceptOf(Multiple.class));
			String baseType = importTypeOf(c, multiple) + String.join("", facets(c));
			String type = multiple && !c.i$(conceptOf(DataComponents.Image.class)) ? "multiple" : baseType;
			boolean isProjectComponent = isProjectComponent(c);
			String key = keyOf(c, type);
			String importType = isProjectComponent(c) ? ProjectComponentImport : AlexandriaComponentImport;
			registerConcreteImports(c, builder);
			registerMultipleImport(imported, multiple, type, c, builder);
			if (key != null && !imported.contains(key))
				builder.add(importType, importOf(c, importType, isProjectComponent ? false : multiple));
			if (key != null) imported.add(key);
			if (c.i$(conceptOf(CatalogComponents.Moldable.class))) registerMoldableImports(imported, c, builder);
			if (c.i$(conceptOf(HelperComponents.Row.class))) registerRowImports(imported, c, builder);
			if (!c.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) && !c.i$(conceptOf(io.intino.konos.dsl.Template.class)))
				addComponentsImports(imported, components(c), builder);
		});
	}

	protected void registerConcreteImports(Component component, FrameBuilder builder) {
		if (component.i$(conceptOf(TemplateStamp.class)) && !builder.contains("alexandriaTemplateStampImport"))
			builder.add("alexandriaTemplateStampImport", new FrameBuilder("alexandriaImport").add("name", "TemplateStamp"));
		if (component.i$(conceptOf(DisplayStamp.class)) && !builder.contains("alexandriaDisplayStampImport"))
			builder.add("alexandriaDisplayStampImport", new FrameBuilder("alexandriaImport").add("name", "DisplayStamp"));
	}

	protected boolean isProjectComponent(Component component) {
		if (component.i$(conceptOf(ExternalTemplateStamp.class))) return false;
		if (component.i$(conceptOf(LibraryTemplateStamp.class))) return false;
		if (component.i$(conceptOf(BaseStamp.class))) return true;
		if (component.i$(conceptOf(HelperComponents.Row.class))) return true;
		return component.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class));
	}

	protected boolean isEmbeddedComponent(Component component) {
		if (!component.i$(conceptOf(CatalogComponents.Collection.class))) return false;
		return component.core$().graph().rootList().contains(component.core$());
	}

	private String keyOf(Component component, String type) {
		if (component == null) return type;
		if (component.i$(conceptOf(InheritTemplateStamp.class)))
			return component.a$(InheritTemplateStamp.class).template();
		if (component.i$(conceptOf(TemplateStamp.class))) {
			io.intino.konos.dsl.Template template = component.a$(TemplateStamp.class).template();
			return template != null ? template.name$() : null;
		}
		if (component.i$(conceptOf(DisplayStamp.class))) {
			Display display = component.a$(DisplayStamp.class).display();
			return display != null ? display.name$() : null;
		}
		return type;
	}

	private void registerMultipleImport(Set<String> imported, boolean multiple, String type, Component component, FrameBuilder builder) {
		if (!multiple || imported.contains(type)) return;
		builder.add(AlexandriaComponentImport, importOf(component, AlexandriaComponentImport, true));
		imported.add(type);
	}

	private void registerRowImports(Set<String> imported, Component component, FrameBuilder builder) {
		addComponentsImports(imported, component.a$(HelperComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(toList()), builder);
	}

	private void registerMoldableImports(Set<String> imported, Component component, FrameBuilder builder) {
		if (component.i$(conceptOf(CatalogComponents.Table.class)) || component.i$(conceptOf(CatalogComponents.DynamicTable.class))) {
			String name = component.name$().toLowerCase();
			addComponentsImports(imported, component.graph().rowsDisplays(context.graphName()).stream().filter(r -> r.name$().toLowerCase().startsWith(name)).map(r -> r.a$(Component.class)).collect(toList()), builder);
		} else
			addComponentsImports(imported, component.a$(CatalogComponents.Moldable.class).moldList().stream().map(CatalogComponents.Moldable.Mold::item).collect(toList()), builder);
	}

	public Frame frameOf(Layer element, Request request, String packageName) {
		final FrameBuilder result = new FrameBuilder().add("request");
		result.add("display", element.name$());
		if (request.responseType().equals(Asset)) result.add("asset");
		if (request.isFile()) result.add("file");
		result.add("name", request.name$());
		if (request.isType()) {
			final FrameBuilder parameterFrame = buildBaseFrame().add("parameter")
					.add(request.asType().type())
					.add(request.asType().getClass().getSimpleName().replace("Data", ""))
					.add("value", parameter(request, packageName))
					.add("type", request.asType().type());
			if (request.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
			result.add("parameterType", request.asType().type());
			result.add("customParameterType", customParameterType(request));
			result.add("parameterSignature", "value");
			if (request.isFile()) result.add("progressCallback", "progressCallback");
		} else result.add("nullParameter", "null");
		if (request.responseType() == Asset) {
			result.add("method", new FrameBuilder().add("download", "download"));
			result.add("returnType", new FrameBuilder().add("download", "download"));
		}
		else if (request.isFile()) result.add("method", new FrameBuilder().add("upload", "upload"));
		else result.add("method", new FrameBuilder());
		return result.toFrame();
	}

	private static FrameBuilder customParameterType(Request request) {
		FrameBuilder result = new FrameBuilder("parameterType", request.asType().getClass().getSimpleName().replace("Data", ""));
		if (request.isList()) result.add("list");
		if (request.isFile()) result.add("progressCallback", "progressCallback");
		result.add("value", request.asType().type());
		return result;
	}

	private static String parameter(Request request, String packageName) {
		return request.isObject() ? packageName.toLowerCase() + ".schemas." + request.asType().type() : request.asType().type();
	}
}
