package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Components.Block;
import io.intino.konos.model.graph.center.components.CenterBlock;
import io.intino.konos.model.graph.centerjustified.components.CenterJustifiedBlock;
import io.intino.konos.model.graph.endjustified.components.EndJustifiedBlock;
import io.intino.konos.model.graph.flexible.components.FlexibleBlock;
import io.intino.konos.model.graph.horizontal.components.HorizontalBlock;
import io.intino.konos.model.graph.startjustified.components.StartJustifiedBlock;
import io.intino.konos.model.graph.vertical.components.VerticalBlock;
import org.siani.itrules.model.Frame;

import java.util.ArrayList;
import java.util.List;

public class BlockRenderer extends ComponentRenderer<Block> {

	public BlockRenderer(Settings settings, Block component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	protected Frame properties() {
		Frame result = super.properties();
		addLayout(result);
		addPaper(result);
		return result;
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

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("block", "");
	}
}
