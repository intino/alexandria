package io.intino.konos.builder.codegeneration.services.ui.display;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static java.util.stream.Collectors.toList;

public class DisplaysRenderer extends UIRenderer {
	private final File gen;
	private final List<Display> displays;

	public DisplaysRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		super(boxName, packageName);
		this.gen = gen;
		this.displays = graph.displayList().stream().filter(d -> !d.getClass().equals(Display.class)).collect(toList());
	}

	public void execute() {
		if (displays.isEmpty()) return;
		Frame frame = buildFrame();
		for (Display display : displays) frame.addSlot("display", displayFrame(display));
		write(frame);
	}


	private Frame displayFrame(Display display) {
		return new Frame("display")
				.addSlot("name", display.name$())
				.addSlot("type", display.getClass().getSimpleName());
	}

	private void write(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase("Displays");
		writeFrame(new File(gen, DISPLAYS), newDisplay, template().format(frame));
	}

	private Template template() {
		return Formatters.customize(DisplaysTemplate.create());
	}

	protected Frame buildFrame() {
		return super.buildFrame().addTypes("displays");
	}
}
