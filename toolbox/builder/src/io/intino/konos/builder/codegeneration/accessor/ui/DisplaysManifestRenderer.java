package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.DisplaysManifestTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;

import static io.intino.konos.model.graph.KonosGraph.rootDisplays;

public class DisplaysManifestRenderer extends UIRenderer {
	private final UIService service;

	protected DisplaysManifestRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		FrameBuilder result = new FrameBuilder("manifest");
		rootDisplays(service).stream().filter(this::isGeneric).distinct().forEach(d -> result.add("display", display(d)));
		Commons.write(new File(accessorGen() + File.separator + "Displays.js").toPath(), setup(new DisplaysManifestTemplate()).render(result.toFrame()));
	}

	private boolean isGeneric(Display element) {
		return element.getClass().getSimpleName().equalsIgnoreCase("display") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("component") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("template") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("block") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("item") ||
			   element.getClass().getSimpleName().equalsIgnoreCase("row");
	}

	private Frame display(Display display) {
		FrameBuilder result = new FrameBuilder("display", typeOf(display));
		result.add("name", nameOf(display));
		result.add("directory", display.isDecorated() ? "src" : "gen");
		return result.toFrame();
	}

}
