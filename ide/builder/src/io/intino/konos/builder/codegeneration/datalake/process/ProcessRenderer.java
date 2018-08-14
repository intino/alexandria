package io.intino.konos.builder.codegeneration.datalake.process;

import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Procedure;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public class ProcessRenderer {

	private final List<Procedure.Process> processes;
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
		for (Procedure.Process process : processes) {
			final String procedure = process.core$().ownerAs(Procedure.class).name$();
			final String name = composedName(process);
			final Frame frame = new Frame().addTypes("process").
					addSlot("box", boxName).
					addSlot("procedure", procedure).
					addSlot("package", packageName).
					addSlot("name", name);
			if (process.input().schema() != null) {
				frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
				frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", process.input().schema().name$()));
			} else frame.addSlot("type", "message");
			final File destination = new File(src, "procedures" + File.separator + procedure.toLowerCase());
			final String handlerName = firstUpperCase(name) + "Process";
			classes.put(process.getClass().getSimpleName() + "#" + process.name$(), "ness.procedures." + procedure.toLowerCase() + "." + handlerName);
			if (!alreadyRendered(destination, handlerName))
				Commons.writeFrame(destination, handlerName, customize(ProcessTemplate.create()).format(frame));
		}
	}

	private String composedName(Procedure.Process process) {
		return firstUpperCase((process.input().subdomain().isEmpty() ? "" : snakeCaseToCamelCase().format(process.input().subdomain().replace(".", "_"))) + firstUpperCase(process.name$()));
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}

	private List<Procedure.Process> processes(KonosGraph graph) {
		if (graph == null) return Collections.emptyList();
		return graph.procedureList().stream().map(Procedure::processList).flatMap(Collection::stream).collect(Collectors.toList());
	}
}