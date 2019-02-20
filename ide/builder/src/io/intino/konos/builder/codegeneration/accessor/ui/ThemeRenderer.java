package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.ThemeTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Theme;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.write;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ThemeRenderer extends UIRenderer {
	private final UIService service;

	protected ThemeRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void execute() {
		Frame frame = new Frame().addTypes("theme");
		Theme theme = service.theme();
		frame.addSlot("paletteRule", paletteRules(theme).stream().map(this::frameOf).toArray());
		frame.addSlot("customRule", customRules(theme).stream().map(this::frameOf).toArray());
		write(new File(accessorGen() + File.separator + "Theme.js").toPath(), setup(ThemeTemplate.create()).format(frame));
	}

	private List<Theme.Rule> paletteRules(Theme theme) {
		if (theme == null) return emptyList();
		return theme.ruleList().stream().filter(this::isPaletteRule).collect(toList());
	}

	private List<Theme.Rule> customRules(Theme theme) {
		if (theme == null) return emptyList();
		return theme.ruleList().stream().filter(r -> !isPaletteRule(r)).collect(toList());
	}

	private boolean isPaletteRule(Theme.Rule r) {
		return r.i$(Theme.Primary.class) || r.i$(Theme.Secondary.class) || r.i$(Theme.Error.class) || r.i$(Theme.Message.class);
	}

	private Frame frameOf(Theme.Rule rule) {
		Frame result = new Frame().addTypes("rule");
		result.addSlot("name", rule.name$());
		result.addSlot("type", rule.getClass().getSimpleName().toLowerCase());
		result.addSlot("content", rule.content());
		return result;
	}

}
