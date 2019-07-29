package io.intino.konos.builder.codegeneration.process;

import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Process;

import java.util.List;

public class ProcessRenderer extends Renderer {
	private final Settings settings;
	private final List<Process> processes;

	public ProcessRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		this.settings = settings;
		this.processes = graph.processList();
	}

	@Override
	protected void render() {
		for (Process process : processes) {
		}
	}

}