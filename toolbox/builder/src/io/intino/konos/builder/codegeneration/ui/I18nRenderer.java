package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.I18nTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Translator;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;
import java.util.List;

public class I18nRenderer extends UIRenderer {
	private final UIService service;

	public I18nRenderer(Settings settings, UIService service, Target target) {
		super(settings, target);
		this.service = service;
	}

	@Override
	public void execute() {
		FrameBuilder builder = frameBuilder();
		Commons.write(fileOf(folder(), "I18n").toPath(), setup(template()).render(builder.toFrame()));
	}

	private File folder() {
		return new File(gen().getAbsolutePath() + (target == Target.Service ? File.separator + UI : ""));
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder frame = super.frameBuilder().add("i18n");
		List<Translator> translators = service.graph().translatorList();
		translators.forEach(t -> frame.add("translator", frameOf(t)));
		return frame;
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
