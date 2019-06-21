package io.intino.konos.builder.codegeneration.datahub.process;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Procedure;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

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
			final FrameBuilder builder = new FrameBuilder("process").
					add("box", boxName).
					add("procedure", procedure).
					add("package", packageName).
					add("name", name);
			if (process.input().schema() != null) {
				builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName).toFrame());
				builder.add("type", new FrameBuilder("schema").add("package", packageName).add("name", process.input().schema().name$()).toFrame());
			} else builder.add("type", "message");
			final File destination = new File(src, "procedures" + File.separator + procedure.toLowerCase());
			final String handlerName = firstUpperCase(name) + "Process";
			classes.put(process.getClass().getSimpleName() + "#" + process.name$(), "procedures." + procedure.toLowerCase() + "." + handlerName);
			if (!alreadyRendered(destination, handlerName))
				writeFrame(destination, handlerName, customize(new ProcessTemplate()).render(builder.toFrame()));
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