package io.intino.konos.builder.codegeneration.datalake;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class NessJMXOperationsRenderer {
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final boolean hasGraph;
	private File src;

	public NessJMXOperationsRenderer(File gen, File src, String packageName, String boxName, boolean hasGraph) {
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.hasGraph = hasGraph;
	}

	public void execute() {
		FrameBuilder builder = new FrameBuilder("operations").
				add("package", packageName).
				add("box", boxName);
		if (hasGraph) builder.add("graph");
		writeFrame(new File(gen, "datalake"), "NessOperations", operationsTemplate().render(builder.toFrame()));
		writeFrame(new File(gen, "datalake"), "NessOperationsMBean", operationsTemplate().render(builder.add("interface").toFrame()));
		if (!Commons.javaFile(new File(src, "datalake"), "ReflowAssistant").exists())
			writeFrame(new File(src, "datalake"), "ReflowAssistant", assistantTemplate().render(builder.toFrame()));
	}

	private Template operationsTemplate() {
		return Formatters.customize(new NessJMXOperationsTemplate());
	}

	private Template assistantTemplate() {
		return Formatters.customize(new ReflowAssistantTemplate());
	}
}
