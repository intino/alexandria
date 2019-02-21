package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.center.CenterBlock;
import io.intino.konos.model.graph.centerjustified.CenterJustifiedBlock;
import io.intino.konos.model.graph.endjustified.EndJustifiedBlock;
import io.intino.konos.model.graph.flexible.FlexibleBlock;
import io.intino.konos.model.graph.horizontal.HorizontalBlock;
import io.intino.konos.model.graph.startjustified.StartJustifiedBlock;
import io.intino.konos.model.graph.vertical.VerticalBlock;
import org.siani.itrules.model.Frame;

import java.util.ArrayList;
import java.util.List;

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
		if (!element.isSized()) return;
		result.addSlot("width", element.asSized().width());
	}

	private void addLayout(Frame result) {
		List<String> layout = new ArrayList<>();

		if (element.isHorizontal()) layout.add(className(HorizontalBlock.class));
		if (element.isVertical()) layout.add(className(VerticalBlock.class));
		if (element.isCenter()) layout.add(className(CenterBlock.class));
		if (element.isCenterJustified()) layout.add(className(CenterJustifiedBlock.class));
		if (element.isFlexible()) layout.add(className(FlexibleBlock.class));
		if (element.isStartJustified()) layout.add(className(StartJustifiedBlock.class));
		if (element.isEndJustified()) layout.add(className(EndJustifiedBlock.class));
		if (layout.isEmpty()) layout.add(className(VerticalBlock.class));

		result.addSlot("layout", layout.toArray(new String[0]));
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
