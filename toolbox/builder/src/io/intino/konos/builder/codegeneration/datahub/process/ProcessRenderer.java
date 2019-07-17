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
		}
	}

}