package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.Engine;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.template.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.web.templates.I18nTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Translator;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.fileOf;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.folder;

public class I18nRenderer extends UIRenderer {
	private final Service.UI service;
	private final Target target;
	private final File destination;

	public I18nRenderer(CompilationContext compilationContext, Service.UI service, Target target) {
		this(compilationContext, service, target, null);
	}

	public I18nRenderer(CompilationContext compilationContext, Service.UI service, Target target, File destination) {
		super(compilationContext);
		this.service = service;
		this.target = target;
		this.destination = destination;
	}

	@Override
	public void render() {
		FrameBuilder builder = buildFrame();
		Commons.write(fileOf(destinationFolder(), "I18n", target).toPath(), new Engine(template()).addAll(Formatters.all).render(builder.toFrame()));
		if (target.equals(Target.Service))
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), fileOf(folder(gen(target), "/", target), "I18n", target).getAbsolutePath()));
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
		return target == Target.Service || target == Target.ExposedAccessor ?
				new io.intino.konos.builder.codegeneration.services.ui.templates.I18nTemplate() :
				new I18nTemplate();
	}

	private File destinationFolder() {
		return destination != null ? destination : folder(gen(target), "/", target);
	}
}
