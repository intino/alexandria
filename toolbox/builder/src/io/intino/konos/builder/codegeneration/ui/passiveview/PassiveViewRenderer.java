package io.intino.konos.builder.codegeneration.ui.passiveview;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.ElementRenderer;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.InteractionComponents.Switch;
import io.intino.konos.model.graph.OtherComponents.Selector;
import io.intino.konos.model.graph.PassiveView.Notification;
import io.intino.konos.model.graph.PassiveView.Request;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.displayNotifierFolder;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.displayRequesterFolder;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.model.graph.PassiveView.Request.ResponseType.Asset;
import static java.util.stream.Collectors.toList;

public abstract class PassiveViewRenderer<C extends PassiveView> extends ElementRenderer<C> {

	public static final String ProjectComponentImport = "projectComponentImport";
	public static final String AlexandriaComponentImport = "alexandriaComponentImport";

	protected PassiveViewRenderer(CompilationContext compilationContext, C element, TemplateProvider templateProvider, Target target) {
		super(compilationContext, element, templateProvider, target);
	}

	@Override
	public FrameBuilder buildFrame() {
		return buildFrame(false);
	}

	public FrameBuilder buildFrame(boolean accessible) {
		FrameBuilder result = super.buildFrame();
		if (accessible) result.add("accessible");
		FrameBuilder extensionFrame = extensionFrame(accessible);
		String type = type();
		result.add("id", shortId(element));
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
		result.add("request", framesOfRequests(element.requestList()));
		return result;
	}

	protected void createPassiveViewFiles(FrameBuilder elementBuilder) {
		File requesterFile = javaFile(displayRequesterFolder(gen(), target), nameOfPassiveViewFile(element, elementBuilder.toFrame(), "Requester"));
		if (context.cache().isModified(element) || !requesterFile.exists()) {
			writeNotifier(elementBuilder);
			writeRequester(elementBuilder);
			writePushRequester(elementBuilder);
		}
		if (target.equals(Target.Owner)) {
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(element), requesterFile.getAbsolutePath()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(element), javaFile(displayRequesterFolder(gen(), target), nameOfPassiveViewFile(element, elementBuilder.toFrame(), "PushRequester")).getAbsolutePath()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(element), javaFile(displayNotifierFolder(gen(), target), nameOfPassiveViewFile(element, elementBuilder.toFrame(), "Notifier")).getAbsolutePath()));
		}
	}

	protected String type() {
		return typeOf(element.a$(Display.class));
	}

	protected void writeRequester(PassiveView element, FrameBuilder builder) {
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Requester");
		writeFrame(displayRequesterFolder(gen(), target), name, displayRequesterTemplate(builder).render(frame));
	}

	protected void writePushRequester(PassiveView element, FrameBuilder builder) {
		Frame frame = builder.toFrame();
		Template template = displayPushRequesterTemplate(builder);
		boolean accessible = isAccessible(frame);
		if (accessible || template == null) return;
		String name = nameOfPassiveViewFile(element, frame, "PushRequester");
		writeFrame(displayRequesterFolder(gen(), target), name, template.render(frame));
	}

	protected void writeNotifier(PassiveView element, FrameBuilder builder) {
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Notifier");
		writeFrame(displayNotifierFolder(gen(), target), name, displayNotifierTemplate(builder).render(frame));
	}

	private String nameOfPassiveViewFile(PassiveView element, Frame frame, String suffix) {
		return snakeCaseToCamelCase(element.name$() + (isAccessible(frame) ? "Proxy" : "") + suffix);
	}

	protected void addGeneric(PassiveView element, FrameBuilder builder) {
		if (!isGeneric(element)) return;
		builder.add("generic");
		if (element.isExtensionOf()) builder.add("isExtensionOf");
		builder.add("parent", genericParent(element));
	}

	protected boolean isGeneric(PassiveView element) {
		return element.isExtensionOf() || KonosGraph.isParent(context.graphName(), element);
	}

	protected String genericParent(PassiveView element) {
		if (element.isExtensionOf()) return firstUpperCase(element.asExtensionOf().parentView().name$());
		return (target != Target.Accessor ? "io.intino.alexandria.ui.displays." : "") + firstUpperCase(typeOf(element));
	}

	protected String packageTypeRelativeDirectory(PassiveView passiveView) {
		return typeOf(passiveView).equalsIgnoreCase("display") ? "" : "../";
	}

	protected void addComponentsImports(FrameBuilder builder) {
		if (element.i$(HelperComponents.Row.class))
			addComponentsImports(element.a$(HelperComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(toList()), builder);
		else addComponentsImports(components(element), builder);
	}

	protected void addComponentsImports(List<Component> componentList, FrameBuilder builder) {
		HashSet<String> imported = new HashSet<>();
		addComponentsImports(imported, componentList, builder);
		if (!imported.contains("Block") && element.i$(io.intino.konos.model.graph.Template.class))
			builder.add("alexandriaBlockImport", new FrameBuilder("alexandriaImport").add("name", "Block"));
	}

	protected List<Component> components(PassiveView passiveView) {
		List<Component> components = new ArrayList<>();
		if (passiveView.i$(Block.class)) components.addAll(passiveView.a$(Block.class).componentList());
		if (passiveView.i$(io.intino.konos.model.graph.Template.class))
			components.addAll(passiveView.a$(io.intino.konos.model.graph.Template.class).componentList());
		if (passiveView.i$(OtherComponents.Snackbar.class))
			components.addAll(passiveView.a$(OtherComponents.Snackbar.class).componentList());
		if (passiveView.i$(VisualizationComponents.Stepper.class))
			components.addAll(passiveView.a$(VisualizationComponents.Stepper.class).stepList());
		if (passiveView.i$(VisualizationComponents.Stepper.Step.class))
			components.addAll(passiveView.a$(VisualizationComponents.Stepper.Step.class).componentList());
		if (passiveView.i$(OtherComponents.Header.class))
			components.addAll(passiveView.a$(OtherComponents.Header.class).componentList());
		if (passiveView.i$(Selector.class)) components.addAll(passiveView.a$(Selector.class).componentList());
		if (passiveView.i$(OtherComponents.User.class))
			components.addAll(passiveView.a$(OtherComponents.User.class).componentList());
		if (passiveView.i$(CatalogComponents.Table.class))
			components.addAll(passiveView.a$(CatalogComponents.Table.class).moldList().stream().filter(m -> m.heading() != null).map(CatalogComponents.Collection.Mold::heading).collect(toList()));
		if (passiveView.i$(CatalogComponents.Collection.Mold.Heading.class))
			components.addAll(passiveView.a$(CatalogComponents.Collection.Mold.Heading.class).componentList());
		if (passiveView.i$(CatalogComponents.Collection.Mold.Item.class))
			components.addAll(passiveView.a$(CatalogComponents.Collection.Mold.Item.class).componentList());
		if (passiveView.i$(InteractionComponents.Toolbar.class))
			components.addAll(passiveView.a$(InteractionComponents.Toolbar.class).componentList());
		if (passiveView.i$(OtherComponents.Dialog.class))
			components.addAll(passiveView.a$(OtherComponents.Dialog.class).componentList());
		if (passiveView.i$(OtherComponents.DecisionDialog.class))
			components.add(passiveView.a$(OtherComponents.DecisionDialog.class).selector());
		if (passiveView.i$(OtherComponents.CollectionDialog.class))
			components.add(passiveView.a$(OtherComponents.CollectionDialog.class).collection());
		return components;
	}

	protected void addFacets(PassiveView passiveView, FrameBuilder builder) {
		List<String> facets = facets(passiveView);
		facets.forEach(facet -> builder.add("facet", facet));
	}

	private List<String> facets(PassiveView passiveView) {
		List<String> result = new ArrayList<>();
		if (passiveView.i$(Switch.class)) result.add("Switch");
		if (passiveView.i$(Editable.class)) result.add("Editable");
		if (passiveView.i$(DataComponents.Text.Code.class)) result.add("Code");
		if (passiveView.i$(Block.Drawer.class)) result.add("Drawer");
		if (passiveView.i$(Block.Badge.class)) result.add("Badge");
		if (passiveView.i$(Block.Conditional.class)) result.add("Conditional");
		if (passiveView.i$(Block.Splitter.class)) result.add("Splitter");
		if (passiveView.i$(Selector.Tabs.class)) result.add("Tabs");
		if (passiveView.i$(Selector.Menu.class)) result.add("Menu");
		if (passiveView.i$(Selector.ComboBox.class)) result.add("ComboBox");
		if (passiveView.i$(CatalogComponents.Grouping.ComboBox.class)) result.add("ComboBox");
		if (passiveView.i$(Selector.ListBox.class)) result.add("ListBox");
		if (passiveView.i$(Selector.RadioBox.class)) result.add("RadioBox");
		if (passiveView.i$(Selector.CheckBox.class)) result.add("CheckBox");
		if (passiveView.i$(DataComponents.Image.Avatar.class)) result.add("Avatar");
		if (passiveView.i$(Block.Parallax.class)) result.add("Parallax");
		return result;
	}

	private FrameBuilder importOf(PassiveView passiveView, String container, boolean multiple) {
		FrameBuilder result = new FrameBuilder(container);
		result.add("name", importNameOf(passiveView));
		result.add("type", importTypeOf(passiveView, multiple));
		result.add("directory", directoryOf(passiveView));
		result.add("componentDirectory", componentDirectoryOf(passiveView, multiple));
		if (context.webModuleDirectory().exists()) result.add("webModuleName", context.webModuleDirectory().getName());
		if (!multiple) addFacets(passiveView, result);
		return result;
	}

	private String importNameOf(PassiveView passiveView) {
		if (passiveView.i$(OtherComponents.Stamp.class))
			return passiveView.a$(OtherComponents.Stamp.class).template().name$();
		if (passiveView.i$(OtherComponents.Frame.class))
			return passiveView.a$(OtherComponents.Frame.class).display().name$();
		return nameOf(passiveView);
	}

	protected Object importTypeOf(PassiveView passiveView, boolean multiple) {
		if (multiple) return "multiple";
		if (passiveView.i$(CatalogComponents.Collection.Mold.Item.class) || passiveView.i$(HelperComponents.Row.class))
			return passiveView.name$();
		return typeOf(passiveView);
	}

	protected PassiveView componentOf(PassiveView passiveView) {
		PassiveView component = passiveView;
		if (passiveView.i$(OtherComponents.Stamp.class))
			component = passiveView.a$(OtherComponents.Stamp.class).template();
		if (passiveView.i$(OtherComponents.Frame.class))
			component = passiveView.a$(OtherComponents.Frame.class).display();
		return component;
	}

	private String directoryOf(PassiveView passiveView) {
		PassiveView component = componentOf(passiveView);
		return ElementHelper.isRoot(component) ? "src" : "gen";
	}

	private String componentDirectoryOf(PassiveView passiveView, boolean multiple) {
		if (multiple && passiveView.i$(Multiple.class)) return "components";
		if (passiveView.i$(OtherComponents.Stamp.class))
			return componentDirectoryOf(passiveView.a$(OtherComponents.Stamp.class).template(), multiple);
		if (passiveView.i$(OtherComponents.Frame.class))
			return componentDirectoryOf(passiveView.a$(OtherComponents.Frame.class).display(), multiple);
		if (passiveView.i$(io.intino.konos.model.graph.Template.class)) return "templates";
		if (passiveView.i$(CatalogComponents.Collection.Mold.Item.class)) return "items";
		if (passiveView.i$(HelperComponents.Row.class)) return "rows";
		if (passiveView.i$(Component.class)) return "components";
		return null;
	}

	private void writeRequester(FrameBuilder builder) {
		writeRequester(element, builder);
	}

	private void writePushRequester(FrameBuilder builder) {
		writePushRequester(element, builder);
	}

	private boolean isAccessible(Frame frame) {
		return frame.is("accessible");
	}

	private void writeNotifier(FrameBuilder builder) {
		writeNotifier(element, builder);
	}

	private Template displayNotifierTemplate(FrameBuilder builder) {
		return setup(notifierTemplate(builder));
	}

	private Template displayRequesterTemplate(FrameBuilder builder) {
		return setup(requesterTemplate(builder));
	}

	private Template displayPushRequesterTemplate(FrameBuilder builder) {
		Template template = pushRequesterTemplate(builder);
		return template != null ? setup(template) : null;
	}

	private FrameBuilder extensionFrame(boolean accessible) {
		String type = type();
		FrameBuilder result = new FrameBuilder().add(type, "").add("value", type).add("type", type);
		if (element.isExtensionOf()) {
			result.add("extensionOf", "extensionOf");
			result.add("parent", element.asExtensionOf().parentView().name$());
		}
		if (type.equalsIgnoreCase("Component")) result.add("component", "component");
		else if (accessible && element.i$(Display.Accessible.class)) result.add("accessible", "accessible");
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
			final FrameBuilder parameterFrame = new FrameBuilder().add("parameter")
					.add(notification.getClass().getSimpleName())
					.add(notification.asType().type())
					.add(notification.asType().getClass().getSimpleName().replace("Data", ""))
					.add("value", notification.asType().type());
			if (notification.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
		}
		return result.toFrame();
	}

	private Frame[] framesOfRequests(List<Request> requests) {
		return requests.stream().map(r -> frameOf(element, r, packageName())).toArray(Frame[]::new);
	}

	private Template notifierTemplate(FrameBuilder builder) {
		return templateProvider.notifierTemplate(element, builder);
	}

	private Template requesterTemplate(FrameBuilder builder) {
		return templateProvider.requesterTemplate(element, builder);
	}

	private Template pushRequesterTemplate(FrameBuilder builder) {
		return templateProvider.pushRequesterTemplate(element, builder);
	}

	private void addParentImport(FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder().add("value", type());
		if (isGeneric(element) && element.isExtensionOf()) {
			result.add("parent", genericParent(element));
			result.add("parentDirectory", componentDirectoryOf(element.asExtensionOf().parentView(), false));
		} else if (typeOf(element).equalsIgnoreCase("component")) result.add("baseComponent", "");
		else if (builder.is("accessible")) result.add("accessible", "");
		else if (typeOf(element).equalsIgnoreCase("display")) result.add("baseDisplay", "");
		else if (element.i$(Component.class)) result.add("component", "");
		else if (ElementHelper.isRoot(element)) result.add("abstract", "");
		builder.add("parent", result);
	}

	private void addComponentsImports(Set<String> imported, List<Component> componentList, FrameBuilder builder) {
		componentList.forEach(c -> {
			boolean multiple = c.i$(Multiple.class);
			String baseType = importTypeOf(c, multiple) + String.join("", facets(c));
			String type = multiple ? "multiple" : baseType;
			boolean isProjectComponent = isProjectComponent(c);
			String key = keyOf(c, type);
			String importType = isProjectComponent(c) ? ProjectComponentImport : AlexandriaComponentImport;
			registerConcreteImports(c, builder);
			registerMultipleImport(imported, multiple, type, c, builder);
			if (!imported.contains(key))
				builder.add(importType, importOf(c, importType, isProjectComponent ? false : multiple));
			imported.add(key);
			if (c.i$(CatalogComponents.Collection.class)) registerCollectionImports(imported, c, builder);
			if (c.i$(HelperComponents.Row.class)) registerRowImports(imported, c, builder);
			if (!c.i$(CatalogComponents.Collection.Mold.Item.class))
				addComponentsImports(imported, components(c), builder);
		});
	}

	protected void registerConcreteImports(Component component, FrameBuilder builder) {
		if (component.i$(OtherComponents.Stamp.class) && !builder.contains("alexandriaStampImport"))
			builder.add("alexandriaStampImport", new FrameBuilder("alexandriaImport").add("name", "Stamp"));
		if (component.i$(OtherComponents.Frame.class) && !builder.contains("alexandriaFrameImport"))
			builder.add("alexandriaFrameImport", new FrameBuilder("alexandriaImport").add("name", "Frame"));
	}

	protected boolean isProjectComponent(Component component) {
		if (component.i$(OtherComponents.Stamp.class)) return true;
		if (component.i$(OtherComponents.Frame.class)) return true;
		if (component.i$(HelperComponents.Row.class)) return true;
		if (component.i$(CatalogComponents.Collection.Mold.Item.class)) return true;
		return false;
	}

	private String keyOf(Component component, String type) {
		if (component == null) return type;
		if (component.i$(OtherComponents.Stamp.class))
			return component.a$(OtherComponents.Stamp.class).template().name$();
		if (component.i$(OtherComponents.Frame.class))
			return component.a$(OtherComponents.Frame.class).display().name$();
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

	private void registerCollectionImports(Set<String> imported, Component component, FrameBuilder builder) {
		if (component.i$(CatalogComponents.Table.class))
			addComponentsImports(imported, component.graph().rowsDisplays(context.graphName()).stream().map(r -> r.a$(Component.class)).collect(toList()), builder);
		else
			addComponentsImports(imported, component.a$(CatalogComponents.Collection.class).moldList().stream().map(CatalogComponents.Collection.Mold::item).collect(toList()), builder);
	}

	public static Frame frameOf(Layer element, Request request, String packageName) {
		final FrameBuilder result = new FrameBuilder().add("request");
		result.add("display", element.name$());
		if (request.responseType().equals(Asset)) result.add("asset");
		if (request.isFile()) result.add("file");
		result.add("name", request.name$());
		if (request.isType()) {
			final FrameBuilder parameterFrame = new FrameBuilder().add("parameter")
					.add(request.asType().type())
					.add(request.asType().getClass().getSimpleName().replace("Data", ""))
					.add("value", parameter(request, packageName));
			if (request.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
			result.add("parameterSignature", "value");
		}
		if (request.responseType() == Asset) result.add("method", new FrameBuilder().add("download", "download"));
		else if (request.isFile()) result.add("method", new FrameBuilder().add("upload", "upload"));
		else result.add("method", new FrameBuilder());
		return result.toFrame();
	}

	private static String parameter(Request request, String packageName) {
		return request.isObject() ? packageName.toLowerCase() + ".schemas." + request.asType().type() : request.asType().type();
	}
}
