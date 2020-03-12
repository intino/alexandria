package io.intino.konos.builder.codegeneration.bpm;

import io.intino.alexandria.logger.Logger;
import io.intino.bpmparser.BpmnParser;
import io.intino.bpmparser.Link;
import io.intino.bpmparser.State;
import io.intino.bpmparser.State.Type;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Workflow.Process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.intino.bpmparser.State.Type.Initial;
import static io.intino.bpmparser.State.Type.Terminal;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.*;

public class BpmRenderer extends Renderer {
	private final CompilationContext compilationContext;
	private final List<Process> processes;
	private final File src;
	private final File gen;
	private final KonosGraph graph;
	private List<String> stateServices = new ArrayList<>();

	public BpmRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.compilationContext = compilationContext;
		this.processes = graph.workflow() != null ? graph.workflow().processList() : Collections.emptyList();
		this.src = new File(compilationContext.src(Target.Owner), "bpm");
		this.gen = new File(compilationContext.gen(Target.Owner), "bpm");
		this.graph = graph;
	}

	@Override
	protected void render() {
		renderProcesses();
		renderWorkflow();
	}

	private void renderWorkflow() {
		if (processes.isEmpty()) return;
		FrameBuilder builder = new FrameBuilder("workflow").
				add("box", compilationContext.boxName()).
				add("package", compilationContext.packageName()).
				add("terminal", compilationContext.dataHubManifest().qn).
				add(compilationContext.boxName()).
				add("process", processes.stream().map(this::frameOf).toArray(Frame[]::new));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(graph.workflow()), javaFile(gen, "Workflow").getAbsolutePath()));
		writeFrame(gen, "Workflow", customize(new WorkflowTemplate()).render(builder.toFrame()));
	}

	private Frame frameOf(Process p) {
		return new FrameBuilder("process").add("name", p.name$()).toFrame();
	}

	private void renderProcesses() {
		for (Process process : processes) {
			stateServices.clear();
			File file = process.bpmn() == null ? null : new File(process.bpmn().getFile());
			if (file == null || !file.exists()) continue;
			renderProcess(process, file);
		}
	}

	private void renderProcess(Process process, File file) {
		final FrameBuilder builder = new FrameBuilder("process").
				add("box", compilationContext.boxName()).
				add("package", compilationContext.packageName()).
				add("name", process.name$());
		try {
			BpmnParser bpmnParser = new BpmnParser(new FileInputStream(file));
			for (State state : bpmnParser.states()) {
				builder.add("state", frameOf(state));
				state.links().forEach(link -> builder.add("link", frameOf(state, link)));
			}
			compilationContext.classes().put(process.getClass().getSimpleName() + "#" + process.name$(), "bpm." + process.name$());
			writeFrame(gen, "Abstract" + firstUpperCase(process.name$()), customize(new ProcessTemplate()).render(builder.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(process), javaFile(gen, "Abstract" + firstUpperCase(process.name$())).getAbsolutePath()));
			if (!alreadyRendered(src, process.name$()))
				writeFrame(src, process.name$(), customize(new ProcessTemplate()).render(builder.add("src").toFrame()));
		} catch (FileNotFoundException e) {
			Logger.error(e);
		}
	}


	private FrameBuilder frameOf(State state) {
		List<Type> types = typesOf(state);
		FrameBuilder builder = new FrameBuilder("state").add("method", format(state)).add("label", state.label());
		if (!types.contains(Type.Intermediate))
			builder.add("type", types.stream().map(Enum::name).toArray(String[]::new));
		if (state.taskType().equals(State.TaskType.Service)) stateServices.add(format(state));
		builder.add("taskType", state.taskType());
		return builder;
	}

	private List<Type> typesOf(State state) {
		List<Type> types = new ArrayList<>();
		if (state.type() == Initial) types.add(Initial);
		if (state.links().isEmpty() || state.type() == Terminal) types.add(Terminal);
		else if (!types.contains(Initial)) types.add(Type.Intermediate);
		return types;
	}

	private FrameBuilder frameOf(State state, Link link) {
		return new FrameBuilder("link").add("from", state.label()).add("to", link.state().label()).add("type", link.type().name());
	}

	private String format(State state) {
		return Formatters.snakeCaseToCamelCase().format(Normalizer.normalize(state.label().replaceAll(" |/", "_"), Normalizer.Form.NFKD)).toString();
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}