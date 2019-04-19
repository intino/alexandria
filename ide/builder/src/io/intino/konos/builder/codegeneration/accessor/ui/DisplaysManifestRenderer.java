package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.DisplaysManifestTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.write;
import static io.intino.konos.model.graph.KonosGraph.displaysOf;

public class DisplaysManifestRenderer extends UIRenderer {
	private final UIService service;

	protected DisplaysManifestRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void execute() {
		Frame frame = new Frame().addTypes("manifest");
		displaysOf(service).stream().filter(this::isGeneric).forEach(d -> frame.addSlot("display", frameOf(d)));
		write(new File(accessorGen() + File.separator + "Displays.js").toPath(), setup(DisplaysManifestTemplate.create()).format(frame));
	}

	private boolean isGeneric(Display element) {
		return element.getClass().getSimpleName().equalsIgnoreCase("display") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("component") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("template") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("block") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("item");
	}

	private Frame frameOf(Display display) {
		Frame result = new Frame("display", typeOf(display));
		result.addSlot("name", nameOf(display));
		result.addSlot("directory", display.isDecorated() ? "src" : "gen");
		return result;
	}

}
