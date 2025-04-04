package io.intino.konos.builder;

import io.intino.builder.CompilerConfiguration;
import io.intino.builder.PostCompileActionMessage;
import io.intino.builder.PostCompileConfigurationDependencyActionMessage;
import io.intino.konos.CompilerMessage;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.builder.utils.Version;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Sentinel;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.builder.BuildConstants.PRESENTABLE_MESSAGE;

public class KonosCompiler {
	private final CompilerConfiguration configuration;
	private final List<CompilerMessage> collector;
	private final List<PostCompileActionMessage> postCompileActionMessages;

	public KonosCompiler(CompilerConfiguration configuration, List<CompilerMessage> collector, List<PostCompileActionMessage> postCompileActionMessages) {
		this.configuration = configuration;
		this.collector = collector;
		this.postCompileActionMessages = postCompileActionMessages;
	}

	public List<OutputItem> compile(List<File> sources) {
		List<OutputItem> compiledFiles = Collections.synchronizedList(new ArrayList<>());
		if (configuration.isVerbose())
			configuration.out().println(PRESENTABLE_MESSAGE + "Konosc: Compiling model...");
		GraphLoader graphLoader = new GraphLoader();
		KonosGraph graph = graphLoader.loadGraph(configuration, sources);
		if (graph == null) return compiledFiles;
		if (configuration.isVerbose())
			configuration.out().println(PRESENTABLE_MESSAGE + "Konosc: Rendering classes...");
		CompilationContext context = new CompilationContext(configuration, postCompileActionMessages, sources, compiledFiles);
		try {
			context.loadCache(graph, graphLoader.stashes());
			render(graph, context);
			boolean needRebuild = updateDependencies(requiredDependencies(graph, context));
			if (needRebuild)
				addRebuildNeededMessage(new KonosException("The build has required an updated of dependencies. Please rebuild module for complete compilation"));
			return compiledFiles;
		} catch (Throwable e) {
			processCompilationException(e);
		} finally {
			addWarnings(context.warningMessages());
		}
		return compiledFiles;
	}

	private boolean updateDependencies(Map<String, String> requiredDependencies) {
		if (configuration.groupId().equals("io.intino.alexandria")) return false;
		Map<String, String> currentDependencies = configuration.currentDependencies().stream().collect(Collectors.toMap(d -> d.split(":")[0] + ":" + d.split(":")[1], d -> d.split(":")[2]));
		List<PostCompileConfigurationDependencyActionMessage> toAdd = requiredDependencies.entrySet().stream()
				.filter(entry -> !contains(currentDependencies, entry))
				.map(entry -> new PostCompileConfigurationDependencyActionMessage(configuration.module(), entry.getKey() + ":" + entry.getValue())).toList();
		postCompileActionMessages.addAll(toAdd);
		return !toAdd.isEmpty();
	}

	private boolean contains(Map<String, String> deps, Map.Entry<String, String> entry) {
		try {
			return deps.containsKey(entry.getKey()) && new Version(deps.get(entry.getKey())).compareTo(new Version(entry.getValue())) >= 0;
		} catch (KonosException e) {
			return true;
		}
	}

	private Map<String, String> requiredDependencies(KonosGraph graph, CompilationContext context) {
		Map<String, String> dependencies = Manifest.load().dependencies;
		if (graph.jmxServiceList().isEmpty()) {
			remove(dependencies, "jmx");
			remove(dependencies, "primitives");
		}
		if (graph.messagingServiceList().isEmpty()) remove(dependencies, ":jms");
		if (graph.sentinelList().isEmpty()) remove(dependencies, "scheduler");
		if (graph.messagingServiceList().isEmpty() || context.dataHubManifest() != null)
			remove(dependencies, "terminal-jms");
		if (graph.uiServiceList().isEmpty()) remove(dependencies, "ui");
		if (graph.restServiceList().isEmpty()) remove(dependencies, "rest");
		if (graph.soapServiceList().isEmpty()) remove(dependencies, "soap");
		if (graph.workflow() == null) remove(dependencies, "bpm");
		if (graph.slackBotServiceList().isEmpty()) remove(dependencies, "slack");
		if (graph.cliServiceList().isEmpty()) remove(dependencies, "cli");
		if (graph.visualizationComponents() == null || graph.visualizationComponents().chartList(c -> c.isAbsolute() || c.isRelative()).isEmpty())
			remove(dependencies, "driver-r");
		if (graph.visualizationComponents() == null || graph.visualizationComponents().dashboardList(d -> d.isAbsolute() || d.isRelative()).isEmpty())
			remove(dependencies, "driver-shiny");
		if (graph.actionableComponentsList().isEmpty() || graph.actionableComponentsList().stream().allMatch(l -> l.actionableList(d -> d.isSignDocument() || d.isSignText()).isEmpty()))
			remove(dependencies, "io.intino.icod:core");
		if (graph.sentinelList().stream().noneMatch(Sentinel::isWebHook) ||
				!graph.restServiceList().isEmpty() ||
				!graph.soapServiceList().isEmpty() ||
				!graph.uiServiceList().isEmpty()) remove(dependencies, "http");
		return dependencies;
	}

	private void remove(Map<String, String> dependencies, String type) {
		String toRemove = null;
		for (String dep : dependencies.keySet()) if (dep.contains(type)) toRemove = dep;
		if (toRemove != null) dependencies.remove(toRemove);
	}

	private void render(KonosGraph graph, CompilationContext context) throws KonosException {
		try {

			new FullRenderer(graph, context).execute();
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				throw new KonosException(exceptionAsString, e);
			}
			throw new KonosException(e.getMessage(), e);
		}
	}

	private void processCompilationException(Throwable e) {
		if (e instanceof KonosException) {
			addErrorMessage((KonosException) e);
			return;
		}
		io.intino.alexandria.logger.Logger.error(e.getMessage());
		addMessageWithoutLocation(e.getMessage(), true);
	}

	private void addMessageWithoutLocation(String message, boolean error) {
		collector.add(new CompilerMessage(error ? CompilerMessage.ERROR : CompilerMessage.WARNING, message, null, -1, -1));
	}

	private void addErrorMessage(KonosException exception) {
		collector.add(new CompilerMessage(CompilerMessage.ERROR, exception.getMessage()));
	}

	private void addRebuildNeededMessage(KonosException exception) {
		collector.add(new CompilerMessage(CompilerMessage.REBUILD_NEED, exception.getMessage(), "null", -1, -1));
	}

	private void addWarnings(List<io.intino.konos.builder.context.WarningMessage> warningMessages) {
		warningMessages.forEach(w -> collector.add(new CompilerMessage(CompilerMessage.WARNING, w.message(), w.owner() == null ? "null" : w.owner().getAbsolutePath(), w.line(), w.column())));
	}
}