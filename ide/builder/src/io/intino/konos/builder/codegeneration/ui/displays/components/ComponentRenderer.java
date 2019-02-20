package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Input;
import org.siani.itrules.model.Frame;

import java.util.ArrayList;
import java.util.List;

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
		if (element.i$(Input.class)) frame.addSlot("input", new Frame("input"));
		if (buildReferences) frame.addTypes("reference");
		addComponents(element, frame);
		return frame;
	}

	protected Frame properties() {
		Frame result = new Frame().addTypes("properties", typeOf(element));
		addCommonProperties(result);
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
		if (!component.i$(Block.class)) return;
		List<Component> components = component.a$(Block.class).componentList();
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
		if (component.i$(Input.class)) result.addSlot("input", new Frame("input", typeOf(component)));
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

	private void addCommonProperties(Frame frame) {
		if (element.label() != null) frame.addSlot("label", element.label());
	}
}
