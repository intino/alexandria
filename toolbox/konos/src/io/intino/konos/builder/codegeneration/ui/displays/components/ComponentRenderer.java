package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.builder.utils.LRUCache;
import io.intino.konos.dsl.*;
import io.intino.konos.dsl.CatalogComponents.Moldable.Mold;
import io.intino.konos.dsl.OtherComponents.*;
import io.intino.magritte.framework.Layer;
import joptsimple.internal.Strings;

import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class ComponentRenderer<C extends Component> extends DisplayRenderer<C> {
	private boolean buildChildren = false;
	private boolean decorated;
	private Display owner;
	public static final Map<String, FrameBuilder> componentFrameMap = Collections.synchronizedMap(new LRUCache<>(10000));

	public ComponentRenderer(CompilationContext compilationContext, C component, RendererWriter provider) {
		super(compilationContext, component, provider);
		this.decorated = ElementHelper.isRoot(component);
	}

	@Override
	public final FrameBuilder buildFrame() {
		String frameId = frameId();
		if (componentFrameMap.containsKey(frameId)) return componentFrameMap.get(frameId);
		FrameBuilder builder = super.buildFrame().add("component");
		if (!belongsToAccessible(element)) builder.add("concreteBox", boxName());
		if (isEmbeddedComponent(element)) builder.add("embedded");
		addOwner(builder);
		addProperties(builder);
		if (buildChildren) builder.add("child");
		if (element.i$(Block.class) && element.core$().owner().is(Selector.class)) builder.add("parentIsSelector");
		builder.add("methodName", element.i$(conceptOf(Block.Conditional.class)) && !element.i$(conceptOf(Block.Multiple.class)) ? "initConditional" : "init");
		addSpecificTypes(builder);
		addParentPath(element, builder);
		addComponents(element, builder);
		addReferences(element, builder);
		addFacets(element, builder);
		addExtends(element, builder);
		addImplements(element, builder);
		fill(builder);
		componentFrameMap.put(frameId, builder);
		return builder;
	}

	public static void clearCache() {
		componentFrameMap.clear();
	}

	public void buildChildren(boolean value) {
		this.buildChildren = value;
	}

	public void decorated(boolean value) {
		this.decorated = value;
	}

	public Display owner() {
		return this.owner;
	}

	public void owner(Display owner) {
		this.owner = owner;
	}

	protected void addParentPath(Component component, FrameBuilder builder) {
		Component parentComponent = component.core$().ownerAs(Component.class);
		List<String> parentPath = new ArrayList<>();
		while (parentComponent != null && !ElementHelper.isRoot(parentComponent)
				&& !parentComponent.i$(conceptOf(Mold.Item.class)) && !parentComponent.i$(conceptOf(HelperComponents.Row.class))) {
			parentPath.add(0, shortId(parentComponent));
			parentComponent = parentComponent.core$().ownerAs(Component.class);
		}
		if (!parentPath.isEmpty()) builder.add("parentPath", Strings.join(parentPath, "."));
	}

	protected FrameBuilder addOwner(FrameBuilder builder) {
		if (owner != null)
			builder.add("owner", (ElementHelper.isRoot(owner) ? "Abstract" : "") + firstUpperCase(owner.name$()));
		Component parentComponent = element.core$().ownerAs(Component.class);
		if (element.i$(conceptOf(BaseStamp.class)) && parentComponent != null && !ElementHelper.isRoot(parentComponent))
			builder.add("parentId", shortId(parentComponent));
		return builder;
	}

	@Override
	protected void addDecoratedFrames(FrameBuilder builder) {
		addDecoratedFrames(builder, decorated);
	}

	protected void fill(FrameBuilder builder) {
	}

	private void addComponents(Component component, FrameBuilder builder) {
		addComponentsImports(builder);
		components(component).forEach(c -> {
			Display virtualParent = virtualParent() != null ? virtualParent() : (c.i$(conceptOf(CatalogComponents.Collection.class)) && component.i$(conceptOf(OtherComponents.Selector.CollectionBox.class)) ? component : null);
			FrameBuilder componentBuilder = buildChildren ? childFrame(c, virtualParent) : componentFrame(c, virtualParent);
			builder.add("component", componentBuilder);
		});
	}

	private void addReferences(Component component, FrameBuilder builder) {
		Set<Component> components = new LinkedHashSet<>(references(component));
		if (writer.target() == Target.Service) builder.add("componentReferences", componentReferencesFrame());
		components.forEach(c -> builder.add("reference", referenceFrame(c)));
	}

	private FrameBuilder referenceFrame(Component component) {
		ComponentRenderer<?> renderer = ComponentRendererFactory.renderer(context, component, writer);
		FrameBuilder builder = new FrameBuilder("reference").add(typeOf(component)).add("name", component.name$());
		if (isEmbeddedComponent(component)) builder.add("embedded");
		Component parentComponent = component.core$().ownerAs(Component.class);
		builder.add("box", boxName());
		if (!belongsToAccessible(component)) builder.add("concreteBox", boxName());
		builder.add("id", shortId(component));
		if (parentComponent != null) builder.add("parentId", nameOf(parentComponent));
		builder.add("properties", renderer.properties());
		addOwner(builder);
		addExtends(component, builder);
		return builder;
	}

	protected FrameBuilder childFrame(Component component, Display virtualParent) {
		UIRenderer uiRenderer = componentRenderer(component, virtualParent);
		FrameBuilder frameBuilder = uiRenderer.buildFrame();
		String[] ancestors = ancestors(component);
		Component parent = component.core$().ownerAs(Component.class);
		if (parent != null) frameBuilder.add("parent", nameOf(parent));
		if (isEmbeddedComponent(component)) frameBuilder.add("embedded");
		if (!frameBuilder.contains("ancestors")) frameBuilder.add("ancestors", ancestors);
		if (!frameBuilder.contains("ancestorsNotMe"))
			frameBuilder.add("ancestorsNotMe", ancestors.length > 0 ? Arrays.copyOfRange(ancestors, 1, ancestors.length) : new String[0]);
		frameBuilder.add("value", uiRenderer.buildFrame().add("addType", typeOf(component)));
		return frameBuilder;
	}

	public FrameBuilder properties() {
		FrameBuilder result = new FrameBuilder().add("properties").add(typeOf(element));
		if (element.color() != null && !element.color().isEmpty()) result.add("color", element.color());
		if (element.darkColor() != null && !element.darkColor().isEmpty()) result.add("darkColor", element.darkColor());
		if (element.backgroundColor() != null && !element.backgroundColor().isEmpty()) result.add("backgroundColor", element.backgroundColor());
		if (element.backgroundDarkColor() != null && !element.backgroundDarkColor().isEmpty()) result.add("backgroundDarkColor", element.backgroundDarkColor());
		if (element.isOption()) result.add("name", element.asOption().name$());
		if (element.i$(conceptOf(Component.Labeled.class)))
			result.add("label", element.a$(Component.Labeled.class).label());
		if (!element.visible()) result.add("visible", element.visible());
		if (element.isTraceable()) result.add("traceable", true);
		if (element.i$(conceptOf(Multiple.class))) {
			Multiple abstractMultiple = element.a$(Multiple.class);
			result.add("multiple");
			result.add("instances", nameOf(element));
			result.add("multipleArrangement", abstractMultiple.arrangement().name());
			result.add("multipleSpacing", abstractMultiple.spacing().value());
			result.add("multipleNoItemsMessage", abstractMultiple.noItemsMessage() != null ? abstractMultiple.noItemsMessage() : "");
			result.add("multipleWrapItems", abstractMultiple.wrapItems());
			result.add("multipleEditable", element.i$(conceptOf(Editable.class)));
			result.add("multipleCollapsed", element.a$(Multiple.class).collapsed());
			result.add("multipleMin", abstractMultiple.count() != null ? abstractMultiple.count().min() : 0);
			result.add("multipleMax", abstractMultiple.count() != null ? abstractMultiple.count().max() : -1);
			result.add("multipleAlign", "stretch");
		}
		if (element.format() != null) {
			String[] format = element.format().stream().map(Layer::name$).sorted().toArray(String[]::new);
			result.add("format", format);
		}
		return result;
	}

	protected String className(Class clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	protected String[] ancestors(Component component) {
		List<String> result = new ArrayList<>();
		Component parent = component.core$().ownerAs(Component.class);
		while (parent != null) {
			result.add(0, nameOf(parent));
			if (parent.i$(conceptOf(Mold.Item.class))) break;
			parent = parent.core$().ownerAs(Component.class);
		}
		return result.toArray(new String[0]);
	}

	private UIRenderer componentRenderer(Component component, Display virtualParent) {
		ComponentRenderer<? extends Component> renderer = ComponentRendererFactory.renderer(context, component, writer);
		renderer.buildChildren(true);
		renderer.decorated(decorated);
		renderer.owner(owner);
		renderer.virtualParent(virtualParent);
		return renderer;
	}

	private List<Component> references(Component component) {
		if (element.i$(conceptOf(HelperComponents.Row.class)))
			return element.a$(HelperComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(Collectors.toList());
		if (element.i$(conceptOf(OtherComponents.Selector.CollectionBox.class)) && element.a$(OtherComponents.Selector.CollectionBox.class).source() != null)
			return Collections.emptyList();
		return component.components();
	}

	private void addExtends(Component element, FrameBuilder builder) {
		builder.add("extends", extendsFrame(element));
	}

	protected FrameBuilder extendsFrame(Component element) {
		FrameBuilder result = buildBaseFrame().add("extends");
		if (element.i$(conceptOf(DataComponents.Image.class))) result.add("image");
		if (element.i$(conceptOf(CatalogComponents.Collection.class))) result.add("collection");
		if (element.i$(conceptOf(CatalogComponents.Table.class))) result.add("table");
		if (element.i$(conceptOf(CatalogComponents.DynamicTable.class))) result.add("dynamictable");
		if (element.i$(conceptOf(CatalogComponents.Grid.class))) result.add("grid");
		result.add("name", nameOf(element));
		if (!addSpecificTypes(result)) result.add("type", type());
		addFacets(element, result);
		addDecoratedFrames(result, decorated);
		return result;
	}

	protected boolean addSpecificTypes(FrameBuilder builder) {

		if (element.i$(conceptOf(Multiple.class))) {
			Multiple multiple = element.a$(Multiple.class);
			String message = multiple.noItemsMessage();
			if (message != null) builder.add("noItemsMessage", message);
			FrameBuilder methodsFrame = addOwner(buildBaseFrame()).add("method").add("multiple");
			if (multiple.collapsed()) methodsFrame.add("collapsable");
			methodsFrame.add("componentType", multipleComponentType(element));
			methodsFrame.add("componentName", multipleComponentName(element));
			if (element.i$(conceptOf(OwnerTemplateStamp.class)))
				methodsFrame.add("componentOwnerBox", element.a$(OwnerTemplateStamp.class).owner().name());
			if (element.i$(conceptOf(Editable.class))) {
				methodsFrame.add("editableMethods", new FrameBuilder("editableMethods"));
				if (!isMultipleSpecificComponent(element)) methodsFrame.add("editableClass", editableClassFrame());
			}
			String objectType = multipleObjectType(element);
			if (objectType != null) {
				methodsFrame.add("objectType", objectType);
				if (!objectType.equals("java.lang.Void")) methodsFrame.add("objectTypeValue", "value");
			}
			methodsFrame.add("name", nameOf(element));
			builder.add("methods", methodsFrame);
			builder.add("multiple");
			builder.add("componentType", multipleComponentType(element));
			builder.add("componentName", multipleComponentName(element));
			if (!isMultipleSpecificComponent(element) && element.i$(conceptOf(Editable.class)))
				builder.add("componentPrefix", nameOf(element));
			if (objectType != null) builder.add("objectType", objectType);
		}

		if (element.i$(conceptOf(Template.class))) {
			builder.add("template");
			String modelClass = element.a$(Template.class).modelClass();
			builder.add("componentType", nameOf(element));
			builder.add("objectType", modelClass != null ? modelClass : "java.lang.Void");
			return true;
		}

		if (element.i$(conceptOf(BaseStamp.class)) && !element.i$(conceptOf(DisplayStamp.class))) {
			builder.add("basestamp");
			if (!element.i$(conceptOf(Multiple.class))) builder.add("single");
			if (element.i$(conceptOf(OwnerTemplateStamp.class))) builder.add("ownertemplatestamp");
			if (element.i$(conceptOf(DisplayStamp.class))) builder.add("displaystamp");
			if (element.i$(conceptOf(ProxyStamp.class))) builder.add("proxystamp");
			String templateName = templateName(element.a$(BaseStamp.class));
			builder.add("template", templateName);
			builder.add("type", templateName);
			builder.add("generic", genericOf(element.a$(BaseStamp.class)));
			if (element.i$(conceptOf(OwnerTemplateStamp.class))) {
				Service.UI.Use owner = element.a$(OwnerTemplateStamp.class).owner();
				builder.add("ownerPackage", ownerTemplateStampPackage(owner));
				builder.add("ownerBox", ownerTemplateStampBox(owner));
			}
			return true;
		}

		if (element.i$(conceptOf(Mold.Item.class))) {
			builder.add("item");
			CatalogComponents.Collection collection = element.a$(Mold.Item.class).core$().ownerAs(CatalogComponents.Collection.class);
			builder.add("itemClass", collection.itemClass() != null ? collection.itemClass() : "java.lang.Void");
			return true;
		}

		return false;
	}

	private String ownerTemplateStampBox(Service.UI.Use use) {
		return use.package$() + ".box." + use.name();
	}

	private Object genericOf(BaseStamp stamp) {
		if (stamp.i$(conceptOf(TemplateStamp.class)) && element.a$(TemplateStamp.class).template() != null) {
			boolean parent = KonosGraph.isParent(context.graphName(), element.a$(TemplateStamp.class).template());
			return parent ? "<>" : "";
		}
		return "";
	}

	private String templateName(BaseStamp stamp) {
		if (stamp.i$(conceptOf(OtherComponents.OwnerTemplateStamp.class)))
			return stamp.a$(OwnerTemplateStamp.class).template();
		if (stamp.i$(conceptOf(OtherComponents.TemplateStamp.class))) {
			Template template = stamp.a$(TemplateStamp.class).template();
			return template != null ? template.name$() : null;
		}
		return null;
	}

	private FrameBuilder editableClassFrame() {
		FrameBuilder result = new FrameBuilder("editableClass");
		result.add("componentType", multipleComponentType(element));
		result.add("componentName", multipleComponentName(element));
		addDecoratedFrames(result, decorated);
		result.add("componentProperties", properties().add("componentClass"));
		return result;
	}

	private void addProperties(FrameBuilder builder) {
		FrameBuilder properties = properties();
		if (properties.slots() <= 0) return;
		builder.add("properties", properties());
	}

	protected FrameBuilder fileMethodFrame(String method, String value) {
		FrameBuilder result = new FrameBuilder("fileMethod").add("name", method).add("value", value);
		addOwner(result);
		return result;
	}

	protected FrameBuilder resourceMethodFrame(String method, String value) {
		FrameBuilder result = new FrameBuilder("resourceMethod").add("name", method).add("value", fixResourceValue(value));
		addOwner(result);
		return result;
	}

	protected FrameBuilder parameterMethodFrame(String name, String value) {
		FrameBuilder frame = new FrameBuilder("parameter");
		frame.add("name", name);
		frame.add("value", value);
		addOwner(frame);
		return frame;
	}

	protected String fixResourceValue(String value) {
		return value.startsWith("/") ? value : "/" + value;
	}

	private void addImplements(C element, FrameBuilder builder) {
		if (!element.isOption()) return;
		builder.add("implements", new FrameBuilder("implements", "option").add("option", ""));
	}

	private FrameBuilder componentReferencesFrame() {
		FrameBuilder result = new FrameBuilder("componentReferences");
		List<Component> componentList = null;

		if (element.i$(conceptOf(Block.class))) componentList = element.a$(Block.class).componentList();
		else if (element.i$(conceptOf(Template.class))) componentList = element.a$(Template.class).componentList();
		else if (element.i$(conceptOf(Dialog.class))) componentList = element.a$(Dialog.class).componentList();
		else if (element.i$(conceptOf(Mold.Item.class))) componentList = element.a$(Mold.Item.class).componentList();

		if (componentList == null) return result;

		result.add("forRoot");
		componentList.forEach(c -> addComponent(c, virtualParent(), result));

		return componentList.size() > 0 ? result : null;
	}

	private String frameId() {
		return nameOf(element) + (buildChildren && virtualParent() != null ? virtualParent().name$() : "") + (owner != null ? owner.name$() : "") + writer.target().name() + buildChildren + decorated;
	}

}
