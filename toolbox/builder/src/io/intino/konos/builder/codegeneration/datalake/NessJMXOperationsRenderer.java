package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class NessJMXOperationsRenderer {
	private final File gen;
	private File src;
	private final String packageName;
	private final String boxName;
	private final boolean hasGraph;

	public NessJMXOperationsRenderer(File gen, File src, String packageName, String boxName, boolean hasGraph) {
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.hasGraph = hasGraph;
	}

	public void execute() {
		Frame frame = new Frame("operations").
				addSlot("package", packageName).
				addSlot("box", boxName);
		if (hasGraph) frame.addTypes("graph");
		writeFrame(new File(gen, "datalake"), "NessOperations", operationsTemplate().format(frame));
		writeFrame(new File(gen, "datalake"), "NessOperationsMBean", operationsTemplate().format(frame.addTypes("interface")));
		if (!Commons.javaFile(new File(src, "datalake"), "ReflowAssistant").exists())
			writeFrame(new File(src, "datalake"), "ReflowAssistant", assistantTemplate().format(frame));
	}

	private Template operationsTemplate() {
		return Formatters.customize(NessJMXOperationsTemplate.create());
	}

	private Template assistantTemplate() {
		return Formatters.customize(ReflowAssistantTemplate.create());
	}
}
