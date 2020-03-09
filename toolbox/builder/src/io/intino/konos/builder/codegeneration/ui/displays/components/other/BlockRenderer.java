package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.OtherComponents.Selector;
import io.intino.konos.model.graph.rules.Spacing;

public class BlockRenderer extends SizedRenderer<Block> {

	public BlockRenderer(CompilationContext compilationContext, Block component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		if (element.isConditional()) builder.add("conditional");
		addBinding(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addSpacing(result);
		addLayout(result);
		addPaper(result);
		addBadge(result);
		addParallax(result);
		addDrawer(result);
		addTransition(result);
		addSplitter(result);
		if (element.hidden() != null && element.hidden() != Block.Hidden.Never)
			result.add("hidden", element.hidden().name());
		if (element.isCollapsible()) result.add("collapsible", "true");
		return result;
	}

	private void addSpacing(FrameBuilder builder) {
		if (element.spacing() != Spacing.None) builder.add("spacing", element.spacing().value());
	}

	private void addLayout(FrameBuilder builder) {
		String[] layout = element.layout().stream().map(l -> l.name().toLowerCase()).toArray(String[]::new);
		builder.add("layout", layout);
	}

	private void addPaper(FrameBuilder builder) {
		if (!element.isPaper()) return;
		builder.add("paper", "paper");
	}

	private void addBadge(FrameBuilder builder) {
		if (!element.isBadge()) return;
		Block.Badge badgeBlock = element.asBadge();
		FrameBuilder badgeFrame = new FrameBuilder("badge");
		if (badgeBlock.value() != -1) badgeFrame.add("value", badgeBlock.value());
		if (badgeBlock.max() != -1) badgeFrame.add("max", badgeBlock.max());
		if (badgeBlock.showZero()) badgeFrame.add("showZero", true);
		badgeFrame.add("mode", badgeBlock.mode().name());
		builder.add("badge", badgeFrame);
	}

	private void addParallax(FrameBuilder builder) {
		if (!element.isParallax()) return;
		String background = element.asParallax().background();
		if (background == null || background.isEmpty()) return;
		builder.add("background", resourceMethodFrame("background", background));
	}

	private void addDrawer(FrameBuilder builder) {
		if (!element.isDrawer()) return;
		Block.Drawer drawerBlock = element.asDrawer();
		FrameBuilder drawerFrame = new FrameBuilder("drawer");
		drawerFrame.add("position", drawerBlock.position().name());
		drawerFrame.add("variant", drawerBlock.variant().name());
		builder.add("drawer", drawerFrame);
	}

	private void addTransition(FrameBuilder builder) {
		if (!element.isAnimated()) return;
		Block.Animated block = element.asAnimated();
		Block.Animated.Transition transition = block.transition();
		builder.add("mode", block.mode().name());
		builder.add("transitionDirection", transition != null ? transition.direction().name() : "Right");
		builder.add("transitionDuration", transition != null ? transition.duration() : 500);
	}

	private void addSplitter(FrameBuilder builder) {
		if (!element.isSplitter()) return;
		Block.Splitter block = element.asSplitter();
		builder.add("splitMobileLabel", block.splitMobileLabel());
	}

	private void addBinding(FrameBuilder builder) {
		if (!element.isConditional()) return;

		Component.Option option = element.asConditional().selected();
		if (option == null) return;

		Selector selector = option.core$().ownerAs(Selector.class);
		builder.add("binding", new FrameBuilder("binding")
				.add("name", nameOf(element))
				.add("selector", selector.name$())
				.add("option", option.name$()));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("block", "");
	}
}
