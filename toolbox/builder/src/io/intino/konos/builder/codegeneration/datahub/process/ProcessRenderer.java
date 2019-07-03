package io.intino.konos.builder.codegeneration.datahub.process;

import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Process;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ProcessRenderer {

	private final File src;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;
	private final List<Process> processes;

	public ProcessRenderer(KonosGraph graph, File src, String packageName, String boxName, Map<String, String> classes) {
		this.processes = graph.processList();
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		for (Process process : processes) {
//			final String procedure = process.core$().ownerAs(Procedure.class).name$();
//			final String name = composedName(process);
//			final FrameBuilder builder = new FrameBuilder("process").
//					add("box", boxName).
//					add("procedure", procedure).
//					add("package", packageName).
//					add("name", name);
//			if (process.input().schema() != null) {
//				builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName).toFrame());
//				builder.add("type", new FrameBuilder("schema").add("package", packageName).add("name", process.input().schema().name$()).toFrame());
//			} else builder.add("type", "message");
//			final File destination = new File(src, "procedures" + File.separator + procedure.toLowerCase());
//			final String handlerName = firstUpperCase(name) + "Process";
//			classes.put(process.getClass().getSimpleName() + "#" + process.name$(), "procedures." + procedure.toLowerCase() + "." + handlerName);
//			if (!alreadyRendered(destination, handlerName))
//				writeFrame(destination, handlerName, customize(new ProcessTemplate()).render(builder.toFrame()));
		}
	}

}