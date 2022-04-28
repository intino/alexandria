package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.VisualizationComponents.AppDirectory;

public class AppDirectoryRenderer extends ComponentRenderer<AppDirectory> {

	public AppDirectoryRenderer(CompilationContext compilationContext, AppDirectory component, TemplateProvider provider, Target target) {
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
		AppDirectory.Source source = element.source();
		addFromFileAspect(builder, source);
		addFromResourceAspect(builder, source);
		addInlineAspect(builder, source);
	}

	private void addFromFileAspect(FrameBuilder builder, AppDirectory.Source source) {
		if (!source.isFromFile()) return;
		builder.add("fromFile");
		AppDirectory.Source.FromFile origin = source.asFromFile();
		if (origin.file() != null && !origin.file().isEmpty()) builder.add("source", fileMethodFrame("source", origin.file()).add("extraParam", clean(origin.separator())));
	}

	private String clean(String separator) {
		return separator.replace("\\\\", "\\");
	}

	private void addFromResourceAspect(FrameBuilder builder, AppDirectory.Source source) {
		if (!source.isFromResource()) return;
		builder.add("fromResource");
		AppDirectory.Source.FromResource origin = source.asFromResource();
		if (origin.path() != null && !origin.path().isEmpty()) builder.add("source", resourceMethodFrame("source", origin.path()).add("extraParam", clean(origin.separator())));
	}

	private void addInlineAspect(FrameBuilder builder, AppDirectory.Source source) {
		if (!source.isInline()) return;
		builder.add("inline");
		AppDirectory.Source.Inline origin = source.asInline();
		origin.applicationList().forEach(a -> builder.add("application", applicationFrame(a)));
	}

	private FrameBuilder applicationFrame(AppDirectory.Source.Inline.Application application) {
		FrameBuilder result = new FrameBuilder("appDirectoryApplication");
		result.add("name", application.name());
		result.add("url", application.url());
		application.translationList().forEach(t -> result.add("translation", translationFrame(application, t)));
		return result;
	}

	private Object translationFrame(AppDirectory.Source.Inline.Application application, AppDirectory.Source.Inline.Application.Translation translation) {
		FrameBuilder result = new FrameBuilder("appDirectoryApplicationTranslation");
		result.add("language", translation.language());
		result.add("name", translation.name());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("appDirectory", "");
	}
}
