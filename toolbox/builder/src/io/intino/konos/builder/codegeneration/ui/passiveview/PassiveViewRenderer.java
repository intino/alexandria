package io.intino.konos.builder.codegeneration.ui.passiveview;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.ElementRenderer;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.PassiveView.Notification;
import io.intino.konos.model.graph.PassiveView.Request;
import io.intino.konos.model.graph.avatar.datacomponents.AvatarImage;
import io.intino.konos.model.graph.badge.BadgeBlock;
import io.intino.konos.model.graph.checkbox.othercomponents.CheckBoxSelector;
import io.intino.konos.model.graph.code.datacomponents.CodeText;
import io.intino.konos.model.graph.combobox.catalogcomponents.ComboBoxGrouping;
import io.intino.konos.model.graph.combobox.othercomponents.ComboBoxSelector;
import io.intino.konos.model.graph.conditional.ConditionalBlock;
import io.intino.konos.model.graph.decorated.DecoratedDisplay;
import io.intino.konos.model.graph.menu.othercomponents.MenuSelector;
import io.intino.konos.model.graph.parallax.ParallaxBlock;
import io.intino.konos.model.graph.radiobox.othercomponents.RadioBoxSelector;
import io.intino.tara.magritte.Layer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.displayNotifierFolder;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.displayRequesterFolder;
import static io.intino.konos.model.graph.PassiveView.Request.ResponseType.Asset;

public abstract class PassiveViewRenderer<C extends PassiveView> extends ElementRenderer<C> {

	protected PassiveViewRenderer(Settings settings, C element, TemplateProvider templateProvider, Target target) {
		super(settings, element, templateProvider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder result = super.frameBuilder();
		FrameBuilder extensionFrame = extensionFrame();
		String type = type();
		result.add("id", shortId(element));
		result.add("type", type);
		addAccessorType(result);
		result.add("parentType", extensionFrame);
		result.add("import", extensionFrame);
		if (!type.equalsIgnoreCase("display")) result.add("packageType", type.toLowerCase());
		result.add("packageTypeRelativeDirectory", packageTypeRelativeDirectory(element));
		result.add("name", nameOf(element));
		result.add("notification", framesOfNotifications(element.notificationList()));
		result.add("request", framesOfRequests(element.requestList()));
		return result;
	}

	public static Frame frameOf(Layer element, Request request, String packageName) {
		final FrameBuilder result = new FrameBuilder().add("request");
		result.add("display", element.name$());
		if (request.responseType().equals(Asset)) result.add("asset");
		if (request.isFile()) result.add("file");
		result.add("name", request.name$());
		if (request.isType()) {
			final FrameBuilder parameterFrame = new FrameBuilder().add("parameter").add(request.asType().type()).add(request.asType().getClass().getSimpleName().replace("Data", "")).add("value", parameter(request, packageName));
			if (request.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
			result.add("parameterSignature", "value");
		}
		if (request.responseType() == Asset) result.add("method", new FrameBuilder().add("download", "download"));
		else if (request.isFile()) result.add("method", new FrameBuilder().add("upload", "upload"));
		else result.add("method", new FrameBuilder());
		return result.toFrame();
	}

	protected void createPassiveViewFiles(FrameBuilder elementBuilder) {
		Frame frame = elementBuilder.toFrame();
		writeNotifier(frame);
		writeRequester(frame);
		writePushRequester(frame);
	}

	protected String type() {
		return typeOf(element.a$(Display.class));
	}

	protected void writeRequester(PassiveView element, Frame frame) {
		String name = snakeCaseToCamelCase(element.name$() + (isAccessible(frame) ? "Proxy" : "") + "Requester");
		writeFrame(displayRequesterFolder(gen(), target), name, displayRequesterTemplate().render(frame));
	}

	protected void writePushRequester(PassiveView element, Frame frame) {
		Template template = displayPushRequesterTemplate();
		boolean accessible = isAccessible(frame);
		if (accessible || template == null) return;
		String name = snakeCaseToCamelCase(element.name$() + "PushRequester");
		writeFrame(displayRequesterFolder(gen(), target), name, template.render(frame));
	}

	protected void writeNotifier(PassiveView element, Frame frame) {
		String notifierName = snakeCaseToCamelCase(element.name$() + (isAccessible(frame) ? "Proxy" : "") + "Notifier");
		writeFrame(displayNotifierFolder(gen(), target), notifierName, displayNotifierTemplate().render(frame));
	}

	protected void addGeneric(PassiveView element, FrameBuilder builder) {
		if (!isGeneric(element)) return;
		builder.add("generic");
		if (element.isExtensionOf()) builder.add("isExtensionOf");
		builder.add("parent", genericParent(element));
	}

	protected boolean isGeneric(PassiveView element) {
		return element.isExtensionOf() || (element.i$(Component.class) && element.graph().isParentComponent(element.a$(Component.class)));
	}

	protected String genericParent(PassiveView element) {
		if (element.isExtensionOf()) return firstUpperCase(element.asExtensionOf().parentView().name$());
		return target == Target.Accessor ? "Component" : "io.intino.alexandria.ui.displays.Component";
	}

	protected String packageTypeRelativeDirectory(PassiveView passiveView) {
		return typeOf(passiveView).equalsIgnoreCase("display") ? "" : "../";
	}

	protected void addComponentImports(List<Component> componentList, FrameBuilder builder) {
		HashSet<String> mounted = new HashSet<>();
		addComponentImports(mounted, componentList, builder);
		if (!mounted.contains("Block") && element.i$(io.intino.konos.model.graph.Template.class)) builder.add("alexandriaBlockImport", new FrameBuilder("alexandriaBlockImport"));
	}

	protected List<Component> components(Component component) {
		List<Component> components = new ArrayList<>();
		if (component.i$(Block.class)) components.addAll(component.a$(Block.class).componentList());
		if (component.i$(io.intino.konos.model.graph.Template.class)) components.addAll(component.a$(io.intino.konos.model.graph.Template.class).componentList());
		if (component.i$(OtherComponents.Snackbar.class)) components.addAll(component.a$(OtherComponents.Snackbar.class).componentList());
		if (component.i$(OtherComponents.Wizard.Step.class)) components.addAll(component.a$(OtherComponents.Wizard.Step.class).componentList());
		if (component.i$(OtherComponents.Header.class)) components.addAll(component.a$(OtherComponents.Header.class).componentList());
		if (component.i$(OtherComponents.Selector.class)) components.addAll(component.a$(OtherComponents.Selector.class).componentList());
		if (component.i$(CatalogComponents.Collection.Mold.Heading.class)) components.addAll(component.a$(CatalogComponents.Collection.Mold.Heading.class).componentList());
		if (component.i$(CatalogComponents.Collection.Mold.Item.class)) components.addAll(component.a$(CatalogComponents.Collection.Mold.Item.class).componentList());
		if (component.i$(OperationComponents.Toolbar.class)) components.addAll(component.a$(OperationComponents.Toolbar.class).componentList());
		return components;
	}

	protected void addFacets(Component component, FrameBuilder builder) {
		List<String> facets = facets(component);
		facets.forEach(facet -> builder.add("facet", facet));
	}

	private List<String> facets(Component component) {
		List<String> result = new ArrayList<>();
		if (component.i$(Editable.class)) result.add("Editable");
		if (component.i$(CodeText.class)) result.add("Code");
		if (component.i$(BadgeBlock.class)) result.add("Badge");
		if (component.i$(ConditionalBlock.class)) result.add("Conditional");
		if (component.i$(MenuSelector.class)) result.add("Menu");
		if (component.i$(ComboBoxSelector.class)) result.add("ComboBox");
		if (component.i$(ComboBoxGrouping.class)) result.add("ComboBox");
		if (component.i$(RadioBoxSelector.class)) result.add("RadioBox");
		if (component.i$(CheckBoxSelector.class)) result.add("CheckBox");
		if (component.i$(AvatarImage.class)) result.add("Avatar");
		if (component.i$(ParallaxBlock.class)) result.add("Parallax");
		return result;
	}

	private FrameBuilder componentImport(Component component, String type) {
		FrameBuilder result = new FrameBuilder(type);
		boolean multiple = component.i$(AbstractMultiple.class);
		result.add("name", component.i$(OtherComponents.Stamp.class) ? component.a$(OtherComponents.Stamp.class).template().name$() : nameOf(component));
		result.add("type", multiple ? "multiple" : typeOf(component));
		result.add("directory", component.isDecorated() ? "src" : "gen");
		result.add("componentDirectory", componentDirectory(component));
		if (!multiple) addFacets(component, result);
		return result;
	}

	private String componentDirectory(Component component) {
		if (component.i$(AbstractMultiple.class)) return "components";
		if (component.i$(OtherComponents.Stamp.class)) return componentDirectory(component.a$(OtherComponents.Stamp.class).template());
		if (component.i$(io.intino.konos.model.graph.Template.class)) return "templates";
		if (component.i$(CatalogComponents.Collection.Mold.Item.class)) return "items";
		if (component.i$(PrivateComponents.Row.class)) return "rows";
		if (component.i$(Component.class)) return "components";
		return null;
	}

	private void writeRequester(Frame frame) {
		writeRequester(element, frame);
	}

	private void writePushRequester(Frame frame) {
		writePushRequester(element, frame);
	}

	private boolean isAccessible(Frame frame) {
		return frame.is("accessible");
	}

	private void writeNotifier(Frame frame) {
		writeNotifier(element, frame);
	}

	private Template displayNotifierTemplate() {
		return setup(notifierTemplate());
	}

	private Template displayRequesterTemplate() {
		return setup(requesterTemplate());
	}

	private Template displayPushRequesterTemplate() {
		Template template = pushRequesterTemplate();
		return template != null ? setup(template) : null;
	}

	private FrameBuilder extensionFrame() {
		FrameBuilder result = new FrameBuilder().add(type(), "").add("value", type()).add("type", type());
		if (element.isExtensionOf()) {
			result.add("extensionOf", "extensionOf");
			result.add("parent", element.asExtensionOf().parentView().name$());
		}
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
			final FrameBuilder parameterFrame = new FrameBuilder().add("parameter").add(notification.asType().type()).add(notification.asType().getClass().getSimpleName().replace("Data", "")).add("value", notification.asType().type());
			if (notification.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
		}
		return result.toFrame();
	}

	private Frame[] framesOfRequests(List<Request> requests) {
		return requests.stream().map(r -> frameOf(element, r, packageName())).toArray(Frame[]::new);
	}

	private static String parameter(Request request, String packageName) {
		return request.isObject() ? packageName.toLowerCase() + ".schemas." + request.asType().type() : request.asType().type();
	}

	private Template notifierTemplate() {
		return templateProvider.notifierTemplate(element);
	}

	private Template requesterTemplate() {
		return templateProvider.requesterTemplate(element);
	}

	private Template pushRequesterTemplate() {
		return templateProvider.pushRequesterTemplate(element);
	}

	private void addAccessorType(FrameBuilder builder) {
		FrameBuilder accessorType = new FrameBuilder().add("value", type());
		if (element.getClass().getSimpleName().equalsIgnoreCase("display")) accessorType.add("baseDisplay", "");
		if (element.getClass().getSimpleName().equalsIgnoreCase("component")) accessorType.add("baseComponent", "");
		if (element.i$(Component.class)) accessorType.add("component", "");
		if (element.i$(DecoratedDisplay.class)) accessorType.add("abstract", "");
		builder.add("accessorType", accessorType);
	}

	private void addComponentImports(Set<String> mounted, List<Component> componentList, FrameBuilder builder) {
		componentList.forEach(c -> {
			String type = c.i$(AbstractMultiple.class) ? "multiple" : typeOf(c) + String.join("", facets(c));
			if (c.i$(OtherComponents.Stamp.class) && !c.i$(AbstractMultiple.class)) {
				if (!mounted.contains(c.name$())) builder.add("projectComponentImport", componentImport(c, "projectComponentImport"));
				mounted.add(c.name$());
			}
			else {
				if (!mounted.contains(type)) builder.add("alexandriaComponentImport", componentImport(c, "alexandriaComponentImport"));
				mounted.add(type);
			}
			addComponentImports(mounted, components(c), builder);
		});
	}

}
