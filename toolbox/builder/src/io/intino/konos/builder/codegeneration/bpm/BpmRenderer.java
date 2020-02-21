package io.intino.konos.builder.codegeneration.bpm;

import io.intino.alexandria.logger.Logger;
import io.intino.bpmparser.BpmnParser;
import io.intino.bpmparser.Link;
import io.intino.bpmparser.State;
import io.intino.bpmparser.Task;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Workflow.Process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static io.intino.bpmparser.State.Type.Initial;
import static io.intino.bpmparser.State.Type.Terminal;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.*;

public class BpmRenderer extends Renderer {
	private final CompilationContext compilationContext;
	private final List<Process> processes;
	private final File src;
	private final File gen;
	private final String businessUnit;
	private List<String> stateServices = new ArrayList<>();

	public BpmRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.compilationContext = compilationContext;
		this.businessUnit = graph.workflow() != null ? graph.workflow().businessUnit() : null;
		this.processes = graph.workflow() != null ? graph.workflow().processList() : Collections.emptyList();
		this.src = new File(compilationContext.src(Target.Owner), "bpm");
		this.gen = new File(compilationContext.gen(Target.Owner), "bpm");
	}

	@Override
	protected void render() {
		renderProcesses();
		renderBpm();
	}

	private void renderBpm() {
		if (processes.isEmpty()) return;
		FrameBuilder builder = new FrameBuilder("workflow").add("box", compilationContext.boxName()).add("package", compilationContext.packageName()).add("businessUnit", businessUnit).add(compilationContext.boxName()).add("process", processes.stream().map(p -> frameOf(p)).toArray(Frame[]::new));
		context.compiledFiles().add(new OutputItem(javaFile(gen, "Workflow").getAbsolutePath()));
		writeFrame(gen, "Workflow", customize(new WorkflowTemplate()).render(builder.toFrame()));
	}

	private Frame frameOf(Process p) {
		return new FrameBuilder("process").add("name", p.name$()).toFrame();
	}

	private void renderProcesses() {
		for (Process process : processes) {
			stateServices.clear();
			final FrameBuilder builder = new FrameBuilder("process").
					add("box", compilationContext.boxName()).
					add("package", compilationContext.packageName()).
					add("businessUnit", businessUnit).
					add("name", process.name$());
			File file = new File(process.bpmn().getFile());
			if (!file.exists()) continue;
			try {
				BpmnParser bpmnParser = new BpmnParser(new FileInputStream(file));
				State initial = bpmnParser.getNodeWalker().getInitial().links().get(0).state().type(Initial);
				walk(builder, process, initial);
				for (String stateService : stateServices)
					builder.add("accessor", new FrameBuilder("accessor").add("name", stateService));
				compilationContext.classes().put(process.getClass().getSimpleName() + "#" + process.name$(), "bpm." + process.name$());
				writeFrame(gen, "Abstract" + firstUpperCase(process.name$()), customize(new ProcessTemplate()).render(builder.toFrame()));
				if (!alreadyRendered(src, process.name$()))
					writeFrame(src, process.name$(), customize(new ProcessTemplate()).render(builder.add("src").toFrame()));
			} catch (FileNotFoundException e) {
				Logger.error(e);
			}
		}
	}

	private void walk(FrameBuilder builder, Process process, State initial) {
		Map<String, FrameBuilder> states = new LinkedHashMap<>();
		Map<String, FrameBuilder> links = new LinkedHashMap<>();
		framesFrom(initial, process, states, links);
		builder.add("state", states.values().toArray(new FrameBuilder[0]))
				.add("link", links.values().toArray(new FrameBuilder[0]));
	}

	private void framesFrom(State state, Process process, Map<String, FrameBuilder> states, Map<String, FrameBuilder> links) {
		states.put(state.name(), frameOf(state, process, typeOf(state)));
		for (Link link : state.links()) {
			if (!states.containsKey(link.state().name()) && !link.state().type().equals(Terminal))
				framesFrom(link.state(), process, states, links);
			if (!links.containsKey(state.name() + "#" + link.state().name()))
				links.put(state.name() + "#" + link.state().name(), frameOf(state, link));
		}
	}

	private FrameBuilder frameOf(State state, Process process, List<State.Type> types) {
		FrameBuilder builder = new FrameBuilder("state").add("name", state.name());
		if (!types.contains(State.Type.Intermediate))
			builder.add("type", types.stream().map(Enum::name).toArray(String[]::new));
		if (state.task() != null) {
			if (state.task().type().equals(Task.Type.Service)) stateServices.add(state.name());
			builder.add(state.task().type().name())
					.add("taskType", state.task().type().name())
					.add("taskName", state.task().id())
					.add("businessUnit", businessUnit)
					.add("process", process.name$());
		}
		if (types.contains(Terminal)) state.type(Terminal).links().clear();
		return builder;
	}

	private List<State.Type> typeOf(State state) {
		List<State.Type> types = new ArrayList<>();
		if (state.type().equals(Initial)) types.add(Initial);
		if (state.links().isEmpty() || state.links().get(0).state().type().equals(Terminal)) types.add(Terminal);
		else if (!types.contains(Initial)) types.add(State.Type.Intermediate);
		return types;
	}

	private FrameBuilder frameOf(State state, Link link) {
		return new FrameBuilder("link").add("from", state.name()).add("to", link.state().name()).add("type", link.type().name());
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}