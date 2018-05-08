package io.intino.konos.builder.codegeneration.datalake.process;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Process;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProcessRenderer {

	private final List<Process> processes;
	private final File src;
	private final String packageName;
	private final String boxName;

	public ProcessRenderer(KonosGraph graph, File src, String packageName, String boxName) {
		this.processes = !graph.nessClientList().isEmpty() ? graph.nessClient(0).messageHandlerList().stream().filter(h -> h.i$(Process.class)).map(h -> h.a$(Process.class)).collect(toList()) : Collections.emptyList();
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		for (Process process : processes) {
			final String name = process.schema() != null ? process.schema().name$() : process.name$();
			final Frame frame = new Frame().addTypes("process").
					addSlot("box", boxName).
					addSlot("package", packageName).
					addSlot("name", name);
			if (process.schema() != null) {
				frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
				frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", process.schema().name$()));
			} else frame.addSlot("type", "message");
			if (!process.postConditionList().isEmpty()) frame.addSlot("postcondition", postConditions(process.postConditionList()));
			final File destination = new File(src, "ness/processes");
			final String handlerName = Formatters.firstUpperCase(name) + "Process";
			if (!alreadyRendered(destination, handlerName)) Commons.writeFrame(destination, handlerName,
					Formatters.customize(ProcessTemplate.create()).format(frame));
		}
	}

	private Frame[] postConditions(List<Process.PostCondition> postConditions) {
		return postConditions.stream().map(p -> new Frame("postCondition", p.getClass().getSimpleName())).toArray(Frame[]::new);
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}