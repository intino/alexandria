package io.intino.pandora.plugin.codegeneration.server.ui.display;

import io.intino.pandora.plugin.Activity;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class DisplayNotifierRenderer {

	private static final String DISPLAYS = "displays";
	private final File gen;
	private final String packageName;
	private final List<Activity.Display> displays;

	public DisplayNotifierRenderer(File gen, Graph graph, String packageName) {
		this.gen = gen;
		this.packageName = packageName;
		this.displays = graph.find(Activity.Display.class);
	}

	public void execute() {
		displays.forEach(this::processDisplay);
	}

	private void processDisplay(Activity.Display display) {
		Frame frame = new Frame().addTypes("display");
		writeFrame(new File(gen, DISPLAYS), snakeCaseToCamelCase(operation.concept().name() + "_" + resource.name()) + "Resource", template().format(frame));
	}

}
