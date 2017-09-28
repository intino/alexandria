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


	public NessJMXOperationsRenderer(File gen, File src, String packageName, String boxName) {
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		Frame frame = new Frame("operations").
				addSlot("package", packageName).
				addSlot("box", boxName);
		writeFrame(new File(gen, "ness"), "NessOperations", operationsTemplate().format(frame));
		writeFrame(new File(gen, "ness"), "NessOperationsMBean", operationsTemplate().format(frame.addTypes("interface")));
		if (!Commons.javaFile(new File(src, "ness"), "ReflowAssistant").exists())
			writeFrame(new File(src, "ness"), "ReflowAssistant", providerTemplate().format(frame));
	}

	private Template operationsTemplate() {
		return Formatters.customize(NessJMXOperationsTemplate.create());
	}

	private Template providerTemplate() {
		return Formatters.customize(ReflowAssistantTemplate.create());
	}
}
