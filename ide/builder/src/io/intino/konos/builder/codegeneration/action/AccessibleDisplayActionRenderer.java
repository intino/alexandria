package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AccessibleDisplayActionRenderer extends ActionRenderer {

	private final AccessibleDisplay display;
	private final Map<String, String> classes;

	public AccessibleDisplayActionRenderer(Project project, AccessibleDisplay display, File src, String packageName, String boxName, Map<String, String> classes) {
		super(project, src, packageName, boxName);
		this.display = display;
		this.classes = classes;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action", "ui", "accessibleDisplay");
		frame.addSlot("name", display.name$());
		frame.addSlot("display", display.name$());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		frame.addSlot("parameter", parameters());
		classes.put(display.getClass().getSimpleName() + "#" + firstUpperCase(display.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(display.name$())) + "Action");
		if (!alreadyRendered(destiny, display.name$() + "Proxy"))
			writeFrame(destinyPackage(destiny), display.name$() + "ProxyAction", template().format(frame));
	}

	private Frame[] parameters() {
		List<String> parameters = display.parameters();
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("type", "String")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}

}
