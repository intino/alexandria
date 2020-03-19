package io.intino.konos.builder;

import cottons.utils.Files;
import io.intino.konos.CompilerMessage;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.compiler.shared.PostCompileActionMessage;
import io.intino.konos.compiler.shared.PostCompileConfigurationDependencyActionMessage;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.magritte.io.Stash;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.konos.compiler.shared.KonosBuildConstants.PRESENTABLE_MESSAGE;

public class KonosCompiler {
	private static Map<String, Boolean> firstTimeMap = new HashMap<>();
	private final CompilerConfiguration configuration;
	private final List<CompilerMessage> collector;

	private final List<PostCompileActionMessage> postCompileActionMessages;

	public KonosCompiler(CompilerConfiguration configuration, List<CompilerMessage> collector, List<PostCompileActionMessage> postCompileActionMessages) {
		this.configuration = configuration;
		this.collector = collector;
		this.postCompileActionMessages = postCompileActionMessages;
	}

	public List<OutputItem> compile(List<File> sources) {
		List<OutputItem> compiledFiles = new ArrayList<>();
		try {
			if (configuration.isVerbose())
				configuration.out().println(PRESENTABLE_MESSAGE + "Konosc: Compiling model...");
			GraphLoader graphLoader = new GraphLoader();
			KonosGraph graph = graphLoader.loadGraph(configuration, sources);
			if (graph == null) return compiledFiles;
			if (configuration.isVerbose())
				configuration.out().println(PRESENTABLE_MESSAGE + "Konosc: Rendering classes...");
			render(graph, graphLoader.stashes(), sources, compiledFiles);
			updateDependencies(requiredDependencies(graph));
			return compiledFiles;
		} catch (Exception e) {
			processCompilationException(e);
		} finally {
//			addWarnings(collector);TODO
		}
		return compiledFiles;
	}

	private void updateDependencies(Map<String, String> requiredDependencies) {
		if (configuration.groupId().equals("io.intino.alexandria")) return;
		for (Map.Entry<String, String> entry : requiredDependencies.entrySet())
			postCompileActionMessages.add(new PostCompileConfigurationDependencyActionMessage(configuration.module(), entry.getKey() + ":" + entry.getValue()));
	}

	private Map<String, String> requiredDependencies(KonosGraph graph) {
		Map<String, String> dependencies = Manifest.load().dependencies;
		if (graph.jmxServiceList().isEmpty()) remove(dependencies, "jmx");
		if (graph.messagingServiceList().isEmpty()) remove(dependencies, ":jms");
		if (graph.sentinelList().isEmpty()) remove(dependencies, "scheduler");
		if (graph.datalake() == null) {
			remove(dependencies, "datalake");
			remove(dependencies, "sshj");
		} else if (!graph.datalake().isSshMirrored()) remove(dependencies, "sshj");
		if (graph.messageHub() == null) {
			remove(dependencies, "message");
			remove(dependencies, "terminal-jms");
		}
		if (graph.messageHub() != null && !graph.messageHub().isJmsBus()) remove(dependencies, "terminal-jms");
		if (graph.uiServiceList().isEmpty()) remove(dependencies, "ui");
		if (!graph.uiServiceList().isEmpty() || graph.restServiceList().isEmpty()) remove(dependencies, "rest");
		if (graph.workflow() == null) remove(dependencies, "bpm");
		if (graph.slackBotServiceList().isEmpty()) remove(dependencies, "slack");
		if (graph.visualizationComponents() == null || graph.visualizationComponents().chartList(c -> c.isAbsolute() || c.isRelative()).isEmpty())
			remove(dependencies, "driver-r");
		if (graph.visualizationComponents() == null || graph.visualizationComponents().dashboardList(d -> d.isAbsolute() || d.isRelative()).isEmpty())
			remove(dependencies, "driver-shiny");
		return dependencies;
	}

	private void remove(Map<String, String> dependencies, String type) {
		String toRemove = null;
		for (String dep : dependencies.keySet()) if (dep.contains(type)) toRemove = dep;
		if (toRemove != null) dependencies.remove(toRemove);
	}

	private void render(KonosGraph graph, Stash[] stashes, List<File> sources, List<OutputItem> compiledFiles) throws KonosException {
		try {
			CompilationContext context = new CompilationContext(configuration, postCompileActionMessages, sources, compiledFiles);
			context.loadCache(graph, stashes);
			new FullRenderer(graph, context).execute();
		} catch (Exception e) {
			throw new KonosException(e.getMessage(), e);
		}
	}

	private void processCompilationException(Exception e) {
		if (e instanceof KonosException) {
			addErrorMessage((KonosException) e);
			return;
		}
		io.intino.alexandria.logger.Logger.error(e.getMessage());
		addMessageWithoutLocation(e.getMessage(), true);
	}

	private void addMessageWithoutLocation(String message, boolean error) {
		collector.add(new CompilerMessage(error ? io.intino.magritte.compiler.core.CompilerMessage.ERROR : io.intino.magritte.compiler.core.CompilerMessage.WARNING, message, null, -1, -1));
	}

	private void addErrorMessage(KonosException exception) {
		collector.add(new CompilerMessage(CompilerMessage.ERROR, exception.getMessage(), "null", -1, -1));
	}


	private File prepareIntinoFolder() {
		String intinoFolder = configuration.intinoProjectDirectory() + File.separator + "box" + File.separator + configuration.module();
		File folder = new File(intinoFolder);
		if (folder.exists() && !firstTimeMap.containsKey(intinoFolder)) Files.removeDir(folder);
		if (!folder.exists()) folder.mkdirs();
		firstTimeMap.put(intinoFolder, true);
		return folder;
	}

}