package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AccessibleDisplayActionRenderer extends ActionRenderer {

	private final AccessibleDisplay display;
	private final Map<String, String> classes;

	public AccessibleDisplayActionRenderer(Project project, AccessibleDisplay display, File src, String packageName, String boxName, Map<String, String> classes) {
		super(project, src, packageName, boxName, "accessibleDisplay");
		this.display = display;
		this.classes = classes;
	}

	public void execute() {
		FrameBuilder builder = new FrameBuilder("action", "ui", "accessibleDisplay");
		builder.add("name", display.name$());
		builder.add("display", display.name$());
		builder.add("package", packageName);
		builder.add("box", boxName);
		builder.add("parameter", parameters());
		classes.put(display.getClass().getSimpleName() + "#" + firstUpperCase(display.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(display.name$())) + "Action");
		if (!alreadyRendered(destiny, display.name$() + "Proxy"))
			writeFrame(destinyPackage(destiny), display.name$() + "ProxyAction", template().render(builder.toFrame()));
	}

	private Frame[] parameters() {
		List<String> parameters = display.parameters();
		return parameters.stream().map(parameter -> new FrameBuilder("parameter")
				.add("type", "String")
				.add("name", parameter).toFrame()).toArray(Frame[]::new);
	}

}
