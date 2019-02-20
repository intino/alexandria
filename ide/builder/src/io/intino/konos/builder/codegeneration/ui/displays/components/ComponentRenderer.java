package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.ChildComponents.*;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Editable;
import org.siani.itrules.model.Frame;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ComponentRenderer<C extends Component> extends DisplayRenderer<C> {
	private boolean buildReferences = false;
	private boolean decorated;
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();

	public ComponentRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
		this.decorated = component.isDecorated();
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("component");
		frame.addSlot("id", shortId(element));
		frame.addSlot("properties", properties());
		if (element.i$(Editable.class)) frame.addSlot("editable", new Frame("editable"));
		if (buildReferences) frame.addTypes("reference");
		addComponents(element, frame);
		return frame;
	}

	protected Frame properties() {
		Frame result = new Frame().addTypes("properties", typeOf(element));
		if (element.label() != null) result.addSlot("label", element.label());
		if (element.style() != null) result.addSlot("style", element.style().name$());
		return result;
	}

	public void buildReferences(boolean value) {
		this.buildReferences = value;
	}

	public void decorated(boolean value) {
		this.decorated = value;
	}

	@Override
	protected void addDecoratedFrames(Frame frame) {
		addDecoratedFrames(frame, decorated);
	}

	private void addComponents(Component component, Frame frame) {
		List<Component> components = components(component);
		components.forEach(c -> {
			Frame componentFrame = buildReferences ? referenceFrame(c) : componentFrame(c);
			frame.addSlot( "component", componentFrame);
		});
	}

	@Override
	protected Frame componentFrame(Component component) {
		return componentRenderer(component).buildFrame();
	}

	protected String className(Class clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	private Frame referenceFrame(Component component) {
		Frame result = new Frame().addTypes(typeOf(component), "component");
		if (buildReferences) result.addTypes("reference");
		result.addSlot("id", shortId(component));
		result.addSlot("name", clean(component.name$()));
		result.addSlot("parent", clean(element.name$()));
		result.addSlot("ancestors", ancestors(component));
		result.addSlot("type", typeOf(component));
		if (component.i$(Editable.class)) result.addSlot("editable", new Frame("editable", typeOf(component)));
		result.addSlot("value", componentRenderer(component).buildFrame().addSlot("addType", typeOf(component)));
		addDecoratedFrames(result, decorated);
		addComponents(component, result);
		return result;
	}

	private String[] ancestors(Component component) {
		List<String> result = new ArrayList<>();
		Component parent = component.core$().ownerAs(Component.class);
		while (parent != null) {
			result.add(0, clean(parent.name$()));
			parent = parent.core$().ownerAs(Component.class);
		}
		return result.toArray(new String[0]);
	}

	private UIRenderer componentRenderer(Component component) {
		ComponentRenderer renderer = factory.renderer(settings, component, templateProvider, target);
		renderer.buildReferences(true);
		renderer.decorated(decorated);
		return renderer;
	}

	private List<Component> components(Component component) {
		if (component.i$(Block.class)) return component.a$(Block.class).componentList();
		if (component.i$(Panels.class)) return component.a$(Panels.class).panelList().stream().map(p -> p.a$(Component.class)).collect(toList());
		if (component.i$(Panel.class)) return component.a$(Panel.class).componentList();
		if (component.i$(Tabs.class)) return component.a$(Tabs.class).tabList().stream().map(t -> t.a$(Component.class)).collect(toList());
		if (component.i$(Snackbar.class)) return component.a$(Snackbar.class).componentList();
		if (component.i$(AppBar.class)) return component.a$(AppBar.class).componentList();
		if (component.i$(Content.class)) return component.a$(Content.class).componentList();
		return emptyList();
	}

}
