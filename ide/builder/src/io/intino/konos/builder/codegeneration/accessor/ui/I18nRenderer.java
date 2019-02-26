package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.DictionaryTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Translator;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.write;

public class I18nRenderer extends UIRenderer {
	private final UIService service;

	protected I18nRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void execute() {
		Frame frame = new Frame().addTypes("i18n");
		List<Translator> translators = service.graph().translatorList();
		translators.forEach(t -> frame.addSlot("translator", frameOf(t)));
		write(new File(accessorGen() + File.separator + "i18n.js").toPath(), setup(DictionaryTemplate.create()).format(frame));
	}

	private Frame frameOf(Translator translator) {
		Frame result = new Frame("translator");
		result.addSlot("language", translator.language().code());
		translator.translationList().forEach(t -> result.addSlot("word", frameOf(t)));
		return result;
	}

	private Frame frameOf(Translator.Translation translation) {
		Frame result = new Frame("word");
		result.addSlot("text", translation.text());
		result.addSlot("value", translation.value());
		return result;
	}

}
