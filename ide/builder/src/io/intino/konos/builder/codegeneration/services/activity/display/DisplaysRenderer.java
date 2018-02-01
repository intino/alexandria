package io.intino.konos.builder.codegeneration.services.activity.display;

import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static java.util.stream.Collectors.toList;

public class DisplaysRenderer {
	private static final String DISPLAYS = "displays";

	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Display> displays;

	public DisplaysRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.gen = gen;
		this.packageName = packageName;
		this.displays = graph.displayList().stream().filter(d -> !d.getClass().equals(Display.class)).collect(toList());
		this.boxName = boxName;
	}

	public void execute() {
		if (displays.isEmpty()) return;
		Frame frame = createFrame();
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
		return customize(DisplaysTemplate.create());
	}

	private Template customize(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private Frame createFrame() {
		return new Frame("displays")
				.addSlot("box", boxName)
				.addSlot("package", packageName);
	}
}
