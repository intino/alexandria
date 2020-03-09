package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.I18nTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Translator;

import java.util.List;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.fileOf;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.folder;

public class I18nRenderer extends UIRenderer {
	private final Service.UI service;

	public I18nRenderer(CompilationContext compilationContext, Service.UI service, Target target) {
		super(compilationContext, target);
		this.service = service;
	}

	@Override
	public void render() {
		FrameBuilder builder = buildFrame();
		Commons.write(fileOf(folder(gen(), "/", target), "I18n", target).toPath(), setup(template()).render(builder.toFrame()));
		if (target.equals(Target.Owner))
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), fileOf(folder(gen(), "/", target), "I18n", target).getAbsolutePath()));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder frame = super.buildFrame().add("i18n");
		if (!isAlexandriaUi()) frame.add("use", alexandriaUseFrame());
		service.useList().forEach(use -> frame.add("use", frameOf(use)));
		List<Translator> translators = service.graph().translatorList();
		translators.forEach(t -> frame.add("translator", frameOf(t)));
		return frame;
	}

	private boolean isAlexandriaUi() {
		String groupId = context.configuration().groupId();
		String artifactName = context.configuration().artifactId();
		return groupId.equalsIgnoreCase("io.intino.alexandria") && artifactName.equalsIgnoreCase("ui-framework");
	}

	private Frame alexandriaUseFrame() {
		FrameBuilder result = new FrameBuilder("use");
		result.add("name", "AlexandriaUi");
		result.add("service", "alexandria-ui-elements");
		return result.toFrame();
	}

	private Frame frameOf(Service.UI.Use use) {
		FrameBuilder result = new FrameBuilder("use");
		result.add("name", use.name());
		result.add("service", use.service());
		return result.toFrame();
	}

	private Frame frameOf(Translator translator) {
		FrameBuilder result = new FrameBuilder("translator");
		result.add("language", translator.language().code());
		translator.translationList().forEach(t -> result.add("translation", frameOf(t)));
		return result.toFrame();
	}

	private Frame frameOf(Translator.Translation translation) {
		FrameBuilder result = new FrameBuilder("translation");
		result.add("text", translation.text());
		result.add("value", translation.value());
		return result.toFrame();
	}

	private Template template() {
		return target == Target.Accessor ? new I18nTemplate() : new io.intino.konos.builder.codegeneration.services.ui.templates.I18nTemplate();
	}

}
