package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.VisualizationComponents.Board;

public class BoardRenderer extends ComponentRenderer<Board> {

	public BoardRenderer(CompilationContext compilationContext, Board component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("icon", element.materialIcon());
		addSource(result);
		return result;
	}

	private void addSource(FrameBuilder builder) {
		Board.Source source = element.source();
		addFromFileAspect(builder, source);
		addFromResourceAspect(builder, source);
		addInlineAspect(builder, source);
	}

	private void addFromFileAspect(FrameBuilder builder, Board.Source source) {
		if (!source.isFromFile()) return;
		builder.add("fromFile");
		Board.Source.FromFile origin = source.asFromFile();
		if (origin.file() != null && !origin.file().isEmpty()) builder.add("source", fileMethodFrame("source", origin.file()).add("extraParam", clean(origin.separator())));
	}

	private String clean(String separator) {
		return separator.replace("\\\\", "\\");
	}

	private void addFromResourceAspect(FrameBuilder builder, Board.Source source) {
		if (!source.isFromResource()) return;
		builder.add("fromResource");
		Board.Source.FromResource origin = source.asFromResource();
		if (origin.path() != null && !origin.path().isEmpty()) builder.add("source", resourceMethodFrame("source", origin.path()).add("extraParam", clean(origin.separator())));
	}

	private void addInlineAspect(FrameBuilder builder, Board.Source source) {
		if (!source.isInline()) return;
		builder.add("inline");
		Board.Source.Inline origin = source.asInline();
		origin.applicationList().forEach(a -> builder.add("application", applicationFrame(a)));
	}

	private FrameBuilder applicationFrame(Board.Source.Inline.Application application) {
		FrameBuilder result = new FrameBuilder("boardApplication");
		result.add("name", application.name());
		result.add("url", application.url());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("board", "");
	}
}
