package io.intino.konos.builder;

import cottons.utils.Files;
import io.intino.konos.CompilerMessage;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.cache.CacheReader;
import io.intino.konos.builder.codegeneration.cache.CacheWriter;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.compiler.shared.PostCompileActionMessage;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.io.Stash;

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
	private Map<String, List<String>> outputItems = new HashMap<>();

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
			if (configuration.isVerbose())
				configuration.out().println(PRESENTABLE_MESSAGE + "Konosc: Rendering classes...");
			render(graph, graphLoader.konosStash(), compiledFiles);
			return compiledFiles;
		} catch (Exception e) {
			processCompilationException(e);
		} finally {
//			addWarnings(collector);TODO
		}
		return compiledFiles;
	}

	private void render(KonosGraph graph, Stash stash, List<OutputItem> compiledFiles) throws KonosException {
		try {
			File folder = prepareIntinoFolder();
			io.intino.konos.builder.codegeneration.cache.ElementCache cache = loadCache(folder, graph, stash);
			new FullRenderer(graph, new CompilationContext(configuration, postCompileActionMessages, cache, compiledFiles)).execute();
			saveCache(cache, folder);
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
		collector.add(new CompilerMessage(error ? io.intino.tara.compiler.core.CompilerMessage.ERROR : io.intino.tara.compiler.core.CompilerMessage.WARNING, message, null, -1, -1));
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

	private io.intino.konos.builder.codegeneration.cache.ElementCache loadCache(File folder, KonosGraph graph, Stash stash) {
		return new CacheReader(folder).load(graph, stash);
	}

	private void saveCache(io.intino.konos.builder.codegeneration.cache.ElementCache cache, File folder) {
		new CacheWriter(folder).save(cache);
	}
}