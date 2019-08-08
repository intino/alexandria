package io.intino.konos.builder.codegeneration.bpm;

import io.intino.alexandria.logger.Logger;
import io.intino.bpmparser.BpmnParser;
import io.intino.bpmparser.Link;
import io.intino.bpmparser.State;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Process;
import io.intino.tara.magritte.Layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.intino.bpmparser.State.Type.Initial;
import static io.intino.bpmparser.State.Type.Terminal;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class BpmRenderer extends Renderer {
	private final Settings settings;
	private final List<Process> processes;
	private final File src;
	private final File gen;

	public BpmRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		this.settings = settings;
		this.processes = graph.processList();
		this.src = new File(settings.src(Target.Owner), "bpm");
		this.gen = new File(settings.gen(Target.Owner), "bpm");
	}

	@Override
	protected void render() {
		renderProcesses();
		renderBpm();
	}

	private void renderBpm() {
		if (processes.isEmpty()) return;
		FrameBuilder builder = new FrameBuilder("workflow").add("package", settings.packageName()).add(settings.boxName()).add("process", processes.stream().map(Layer::name$).toArray(String[]::new));
		writeFrame(gen, "Workflow", customize(new WorkflowTemplate()).render(builder.toFrame()));
	}

	private void renderProcesses() {
		for (Process process : processes) {
			final FrameBuilder builder = new FrameBuilder("process").
					add("box", settings.boxName()).
					add("package", settings.packageName()).
					add("name", process.name$());
			File file = new File(process.bpmn().getFile());
			if (!file.exists()) continue;
			try {
				BpmnParser bpmnParser = new BpmnParser(new FileInputStream(file));
				State initial = bpmnParser.getNodeWalker().getInitial().links().get(0).state().type(Initial);
				walk(builder, initial);
				settings.classes().put(process.getClass().getSimpleName() + "#" + process.name$(), "bpm." + process.name$());
				writeFrame(gen, "Abstract" + firstUpperCase(process.name$()), customize(new ProcessTemplate()).render(builder.toFrame()));
				if (!alreadyRendered(src, process.name$()))
					writeFrame(src, process.name$(), customize(new ProcessTemplate()).render(builder.add("src").toFrame()));
			} catch (FileNotFoundException e) {
				Logger.error(e);
			}
		}
	}

	private void walk(FrameBuilder builder, State initial) {
		Map<String, FrameBuilder> states = new LinkedHashMap<>();
		Map<String, FrameBuilder> links = new LinkedHashMap<>();
		framesFrom(initial, states, links);
		builder.add("state", states.values().toArray(new FrameBuilder[0]));
		builder.add("link", links.values().toArray(new FrameBuilder[0]));
	}

	private void framesFrom(State state, Map<String, FrameBuilder> states, Map<String, FrameBuilder> links) {
		states.put(state.name(), frameOf(state, typeOf(state)));
		for (Link link : state.links()) {
			if (!states.containsKey(link.state().name()) && !link.state().type().equals(Terminal))
				framesFrom(link.state(), states, links);
			if (!links.containsKey(state.name() + "#" + link.state().name()))
				links.put(state.name() + "#" + link.state().name(), frameOf(state, link));
		}
	}

	private FrameBuilder frameOf(State state, State.Type type) {
		FrameBuilder builder = new FrameBuilder("state").add("name", state.name());
		if (!State.Type.Intermediate.equals(type)) builder.add("type", type.name());
		if (state.task() != null) {
			builder.add("taskType", state.task().type().name());
			builder.add("taskName", state.task().id());
			builder.add(state.task().type().name());
		}
		if (type.equals(Terminal)) state.type(Terminal).links().clear();
		return builder;
	}

	private State.Type typeOf(State state) {
		if (state.type().equals(Initial)) return Initial;
		if (state.links().isEmpty() || state.links().get(0).state().type().equals(Terminal)) return Terminal;
		return State.Type.Intermediate;
	}

	private FrameBuilder frameOf(State state, Link link) {
		return new FrameBuilder("link").add("from", state.name()).add("to", link.state().name()).add("type", link.type().name());
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}