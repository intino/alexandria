package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Component;
import org.siani.itrules.model.Frame;

import java.util.List;

public class ComponentRenderer extends DisplayRenderer<Component> {
	private final boolean buildReferences;
	private final boolean decorated;

	public ComponentRenderer(Settings settings, Component component, TemplateProvider provider, Target target) {
		this(settings, component, false, component.isDecorated(), provider, target);
	}

	public ComponentRenderer(Settings settings, Component component, boolean buildReferences, TemplateProvider provider, Target target) {
		this(settings, component, buildReferences, component.isDecorated(), provider, target);
	}

	public ComponentRenderer(Settings settings, Component component, boolean buildReferences, boolean decorated, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
		this.buildReferences = buildReferences;
		this.decorated = decorated;
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("component");
		if (buildReferences) frame.addTypes("reference");
		addComponents(frame);
		return frame;
	}

	@Override
	protected void addDecoratedFrames(Frame frame) {
		addDecoratedFrames(frame, decorated);
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
		return new ComponentRenderer(settings, component, true, decorated, templateProvider, target).buildFrame();
	}

	private Frame referenceFrame(Component component) {
		Frame result = new Frame().addTypes(typeOf(component), "component");
		if (buildReferences) result.addTypes("reference");
		result.addSlot("name", clean(component.name$()));
		result.addSlot("parent", clean(element.name$()));
		result.addSlot("type", typeOf(component));
		result.addSlot("value", new ComponentRenderer(settings, component, true, decorated, templateProvider, target).buildFrame().addSlot("addType", typeOf(component)));
		addDecoratedFrames(result, decorated);
		return result;
	}

}
