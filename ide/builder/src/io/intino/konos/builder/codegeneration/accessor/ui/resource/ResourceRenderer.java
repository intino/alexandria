package io.intino.konos.builder.codegeneration.accessor.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PageTemplate;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.write;

public class ResourceRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceRenderer {

	public ResourceRenderer(Settings settings, UIService.Resource resource) {
		super(settings, resource, Target.Accessor);
	}

	@Override
	public void execute() {
		writeHtml(buildFrame());
		writeJavascript(buildFrame());
	}

	private void writeHtml(Frame frame) {
		frame.addTypes("html");
		File file = new File(accessorRoot() + File.separator + resource.name$() + ".html");
		if (file.exists()) return;
		write(file.toPath(), setup(PageTemplate.create()).format(frame));
	}

	private void writeJavascript(Frame frame) {
		frame.addTypes("js");
		File destiny = createIfNotExists(new File(accessorGen() + File.separator + "pages" + File.separator));
		File file = new File(destiny + File.separator + firstUpperCase(resource.name$()) + ".js");
		write(file.toPath(), setup(PageTemplate.create()).format(frame));
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("resource");
		frame.addSlot("name", resource.name$());
		Display display = KonosGraph.templateFor(resource);
		frame.addSlot("pageDisplay", display.name$());
		frame.addSlot("pageDisplayId", shortId(display));
		frame.addSlot("pageDisplayType", typeOf(display));
		addPageDisplayOrigin(frame, display);
		return frame;
	}

	private void addPageDisplayOrigin(Frame frame, Display display) {
		Frame originFrame = new Frame();
		if (display.isDecorated()) originFrame.addSlot("decorated", "");
		frame.addSlot("pageDisplayOrigin", originFrame);
	}

}