package io.intino.konos.builder.codegeneration.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.I18nTemplate;
import io.intino.konos.model.graph.Translator;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.write;

public class I18nRenderer extends UIRenderer {
	private final UIService service;

	public I18nRenderer(Settings settings, UIService service, Target target) {
		super(settings, target);
		this.service = service;
	}

	@Override
	public void execute() {
		Frame frame = buildFrame();
		write(fileOf(folder(), "I18n").toPath(), setup(template()).format(frame));
	}

	private File folder() {
		return new File(gen().getAbsolutePath() + (target == Target.Service ? File.separator + UI : ""));
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("i18n");
		List<Translator> translators = service.graph().translatorList();
		translators.forEach(t -> frame.addSlot("translator", frameOf(t)));
		return frame;
	}

	private Frame frameOf(Translator translator) {
		Frame result = new Frame("translator");
		result.addSlot("language", translator.language().code());
		translator.translationList().forEach(t -> result.addSlot("translation", frameOf(t)));
		return result;
	}

	private Frame frameOf(Translator.Translation translation) {
		Frame result = new Frame("translation");
		result.addSlot("text", translation.text());
		result.addSlot("value", translation.value());
		return result;
	}

	private Template template() {
		return target == Target.Accessor ? I18nTemplate.create() : io.intino.konos.builder.codegeneration.services.ui.templates.I18nTemplate.create();
	}

}
