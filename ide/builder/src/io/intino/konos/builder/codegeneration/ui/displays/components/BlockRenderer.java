package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Block;
import org.siani.itrules.model.Frame;

public class BlockRenderer extends ComponentRenderer<Block> {

	public BlockRenderer(Settings settings, Block component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		addBinding(frame);
		return frame;
	}

	@Override
	protected Frame properties() {
		Frame result = super.properties();
		addSize(result);
		addLayout(result);
		addPaper(result);
		return result;
	}

	private void addSize(Frame result) {
		if (element.isRelative()) {
			result.addSlot("width", element.asRelative().width() + "%");
			result.addSlot("height", element.asRelative().height() + "%");
		}
		else if (element.isAbsolute()) {
			result.addSlot("width", element.asAbsolute().width() + "px");
			result.addSlot("height", element.asAbsolute().height() + "px");
		}
	}

	private void addLayout(Frame result) {
		String[] layout = element.layout().stream().map(l -> l.name().toLowerCase()).toArray(String[]::new);
		result.addSlot("layout", layout);
	}

	private void addPaper(Frame result) {
		if (!element.isPaper()) return;
		result.addSlot("paper", "paper");
	}

	private void addBinding(Frame result) {
		if (!element.isSelectorContainer()) return;
		result.addSlot("binding", new Frame("binding").addSlot("name", element.name$()).addSlot("selector", element.asSelectorContainer().selector().name$()));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("block", "");
	}
}
