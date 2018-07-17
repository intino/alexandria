package io.intino.konos.builder.codegeneration.datalake.process;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.MessageHandler;
import io.intino.konos.model.graph.Process;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static java.util.stream.Collectors.toList;

public class ProcessRenderer {

	private final List<Process> processes;
	private final File src;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public ProcessRenderer(KonosGraph graph, File src, String packageName, String boxName, Map<String, String> classes) {
		this.processes = processes(graph);
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		for (Process process : processes) {
			final String name = composedName(process);
			final Frame frame = new Frame().addTypes("process").
					addSlot("box", boxName).
					addSlot("package", packageName).
					addSlot("name", name);
			if (process.schema() != null) {
				frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
				frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", process.schema().name$()));
			} else frame.addSlot("type", "message");

			final File destination = new File(src, "ness/processes");
			final String handlerName = Formatters.firstUpperCase(name) + "Process";
			classes.put(process.getClass().getSimpleName() + "#" + process.name$(), "ness.processes." + handlerName);
			if (!alreadyRendered(destination, handlerName)) Commons.writeFrame(destination, handlerName,
					Formatters.customize(ProcessTemplate.create()).format(frame));
		}
	}

	private String composedName(MessageHandler handler) {
		return firstUpperCase((handler.subdomain().isEmpty() ? "" : Formatters.snakeCaseToCamelCase().format(handler.subdomain().replace(".", "_"))) + firstUpperCase(name(handler)));
	}

	private String name(MessageHandler handler) {
		return handler.schema() == null ? handler.name$() : handler.schema().name$();
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}

	private List<Process> processes(KonosGraph graph) {
		if (graph == null) return Collections.emptyList();
		return !graph.nessClientList().isEmpty() ? graph.nessClient(0).messageHandlerList().stream().filter(h -> h.i$(Process.class)).map(h -> h.a$(Process.class)).collect(toList()) : Collections.emptyList();
	}
}