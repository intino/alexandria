package io.intino.konos.builder.codegeneration.services.ui.display;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;

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
		FrameBuilder builder = frameBuilder().add("display", displays.stream().map(this::displayFrame).toArray(Frame[]::new));
		write(builder.toFrame());
	}

	private Frame displayFrame(Display display) {
		return new FrameBuilder("display")
				.add("name", display.name$())
				.add("type", display.getClass().getSimpleName()).toFrame();
	}

	private void write(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase("Displays");
		writeFrame(new File(gen, DISPLAYS), newDisplay, template().render(frame));
	}

	private Template template() {
		return Formatters.customize(new DisplaysTemplate());
	}

	protected FrameBuilder frameBuilder() {
		return super.frameBuilder().add("displays");
	}
}