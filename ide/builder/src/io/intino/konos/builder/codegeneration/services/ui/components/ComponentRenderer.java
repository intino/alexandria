package io.intino.konos.builder.codegeneration.services.ui.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.displays.renderers.DisplayRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Component;
import org.siani.itrules.model.Frame;

import java.util.List;

@SuppressWarnings("Duplicates")
public class ComponentRenderer extends DisplayRenderer<Component> {
	private final boolean buildReferences;

	public ComponentRenderer(Settings settings, Component component) {
		this(settings, component, false);
	}

	public ComponentRenderer(Settings settings, Component component, boolean buildReferences) {
		super(settings, component);
		this.buildReferences = buildReferences;
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("component");
		if (buildReferences) frame.addTypes("reference");
		addComponents(frame);
		return frame;
	}

	private void addComponents(Frame frame) {
		if (!element.i$(Block.class)) return;
		List<Component> components = element.a$(Block.class).componentList();
		components.forEach(c -> {
			Frame componentFrame = buildReferences ? referenceFrame(c) : componentFrame(c);
			frame.addSlot( "component", componentFrame);
		});
	}

	private Frame componentFrame(Component component) {
		return new ComponentRenderer(settings, component, true).buildFrame();
	}

	private Frame referenceFrame(Component component) {
		Frame result = new Frame().addTypes(typeOf(component), "component");
		if (buildReferences) result.addTypes("reference");
		result.addSlot("name", clean(component.name$()));
		result.addSlot("parent", clean(element.name$()));
		result.addSlot("type", typeOf(component));
		result.addSlot("value", new ComponentRenderer(settings, component, true).buildFrame().addSlot("addType", typeOf(component)));
		return result;
	}

}
