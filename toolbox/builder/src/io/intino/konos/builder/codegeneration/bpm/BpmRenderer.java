package io.intino.konos.builder.codegeneration.bpm;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.bpm.parser.BpmnParser;
import io.intino.konos.builder.codegeneration.bpm.parser.Link;
import io.intino.konos.builder.codegeneration.bpm.parser.State;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Workflow;
import io.intino.konos.model.Workflow.Process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.bpm.parser.State.Type;
import static io.intino.konos.builder.codegeneration.bpm.parser.State.Type.Initial;
import static io.intino.konos.builder.codegeneration.bpm.parser.State.Type.Terminal;
import static io.intino.konos.builder.helpers.Commons.*;

public class BpmRenderer extends Renderer {
	private final CompilationContext compilationContext;
	private final List<Process> processes;
	private final File src;
	private final File gen;
	private final KonosGraph graph;
	private final Workflow workflow;
	private final List<String> stateServices = new ArrayList<>();

	public BpmRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.compilationContext = compilationContext;
		this.workflow = graph.workflow();
		this.processes = workflow != null ? graph.workflow().processList() : Collections.emptyList();
		this.src = new File(compilationContext.src(Target.Server), "bpm");
		this.gen = new File(compilationContext.gen(Target.Server), "bpm");
		this.graph = graph;
	}

	@Override
	protected void render() throws KonosException {
		renderProcesses();
		renderWorkflow();
	}

	private void renderWorkflow() throws KonosException {
		if (processes.isEmpty()) return;
		if (compilationContext.dataHubManifest() == null)
			throw new KonosException("Is required the Data hub declaration in artifact to instance subscribers");
		FrameBuilder builder = new FrameBuilder("workflow").
				add("box", compilationContext.boxName()).
				add("package", compilationContext.packageName()).
				add("directory", Commons.fileFrame(workflow.directory(), packageName(), context.archetypeQN())).
				add("terminal", compilationContext.dataHubManifest().qn).
				add(compilationContext.boxName()).
				add("process", processes.stream().filter(p -> file(p) != null).map(p -> frameOf(p, file(p))).toArray(FrameBuilder[]::new));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(graph.workflow()), javaFile(gen, "Workflow").getAbsolutePath()));
		writeFrame(gen, "Workflow", customize(new WorkflowTemplate()).render(builder.toFrame()));
	}


	private void renderProcesses() {
		for (Process process : processes) {
			stateServices.clear();
			File file = file(process);
			if (file != null) renderProcess(process, file);
		}
	}

	private File file(Process process) {
		File file = process.bpmn() == null ? null : new File(URLDecoder.decode(process.bpmn().getFile(), Charset.defaultCharset()));
		if (file == null || !file.exists()) {
			return null;
		}
		return file;
	}

	private void renderProcess(Process process, File file) {
		final FrameBuilder builder = frameOf(process, file);
		compilationContext.classes().put(process.getClass().getSimpleName() + "#" + process.name$(), "bpm." + process.name$());
		writeFrame(gen, "Abstract" + firstUpperCase(process.name$()), customize(new ProcessTemplate()).render(builder.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(process), javaFile(gen, "Abstract" + firstUpperCase(process.name$())).getAbsolutePath()));
		if (!alreadyRendered(src, process.name$())) {
			writeFrame(src, process.name$(), customize(new ProcessTemplate()).render(builder.add("src").toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(process), javaFile(src(Target.Server), process.name$()).getAbsolutePath()));
		}
	}

	private FrameBuilder frameOf(Process process, File bpmn) {
		BpmnParser bpmnParser;
		try {
			bpmnParser = new BpmnParser(new FileInputStream(bpmn));
		} catch (FileNotFoundException e) {
			return null;
		}
		final FrameBuilder builder = new FrameBuilder("process").
				add("box", compilationContext.boxName()).
				add("package", compilationContext.packageName()).
				add("name", process.name$());
		List<State> states = bpmnParser.states();
		for (State state : states) {
			builder.add("state", frameOf(state, states));
			state.links().sort((o1, o2) -> Boolean.compare(o1.isDefault(), o2.isDefault()));
			state.links().forEach(link -> builder.add("link", frameOf(state, link)));
			if (state.isInitial() && state.comment() != null)
				Arrays.stream(state.comment().split("\n|\t| ")).
						filter(s -> !s.isEmpty() && !s.isBlank()).map(String::trim).
						filter(l -> l.startsWith("*")).
						forEach(p -> builder.add("parameter", p.trim().substring(1)));
		}
		return builder;
	}

	private FrameBuilder frameOf(State state, List<State> states) {
		List<State.Type> types = typesOf(state);
		FrameBuilder builder = new FrameBuilder("state", entryLink(state, states)).add("method", format(state)).add("label", state.label());
		if (!types.contains(Type.Intermediate))
			builder.add("type", types.stream().map(Enum::name).toArray(String[]::new));
		if (state.taskType() == State.TaskType.Service) stateServices.add(format(state));
		builder.add("taskType", state.taskType());
		return builder;
	}

	private String entryLink(State state, List<State> states) {
		for (State s : states) {
			Link link = s.links().stream().filter(l -> l.to().equals(state)).findFirst().orElse(null);
			if (link != null && link.type() != Link.Type.Line) return "conditional";
		}
		return Link.Type.Line.name();
	}

	private List<Type> typesOf(State state) {
		List<Type> types = new ArrayList<>();
		if (state.isInitial()) types.add(Initial);
		if (state.links().isEmpty() || state.isTerminal()) types.add(Terminal);
		else if (!types.contains(Initial)) types.add(Type.Intermediate);
		return types;
	}

	private FrameBuilder frameOf(State state, Link link) {
		return new FrameBuilder("link").add("from", state.label()).add("to", link.state().label()).add("type", link.isDefault() ? "Default" : link.type().name());
	}

	private String format(State state) {
		return Formatters.snakeCaseToCamelCase().format(Normalizer.normalize(state.label().replaceAll(" |/", "_"), Normalizer.Form.NFKD)).toString();
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}