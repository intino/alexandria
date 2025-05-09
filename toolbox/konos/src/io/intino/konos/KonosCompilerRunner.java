package io.intino.konos;

import io.intino.builder.CompilationInfoExtractor;
import io.intino.builder.CompilerConfiguration;
import io.intino.builder.PostCompileActionMessage;
import io.intino.konos.builder.KonosCompiler;
import io.intino.konos.builder.OutputItem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static io.intino.builder.BuildConstants.*;
import static io.intino.konos.CompilerMessage.ERROR;

class KonosCompilerRunner {
	private static final Logger LOG = Logger.getGlobal();

	private final boolean verbose;
	private PrintStream out = System.out;

	KonosCompilerRunner(boolean verbose) {
		this.verbose = verbose;
	}

	void run(File argsFile) throws Exception {
		final CompilerConfiguration config = new CompilerConfiguration();
		final Map<File, Boolean> sources = new LinkedHashMap<>();
		CompilationInfoExtractor.getInfoFromArgsFile(argsFile, config, sources);
		config.configurationDirectory(new File(config.intinoProjectDirectory(), "konos" + File.separator + config.module()));
		config.setVerbose(verbose);
		config.out(System.out);
		this.out = config.out();
		if (sources.isEmpty()) return;
		if (verbose) out.println(PRESENTABLE_MESSAGE + "Konosc: loading sources...");
		final List<CompilerMessage> messages = new ArrayList<>();
		final List<PostCompileActionMessage> postCompileActionMessages = new ArrayList<>();
		List<OutputItem> compiled = compile(config, sources, messages, postCompileActionMessages);
		if (verbose) report(sources, compiled);
		processErrors(messages);
		if (messages.stream().noneMatch(m -> m.getCategory().equalsIgnoreCase(ERROR)))
			processActions(postCompileActionMessages);
		out.println();
		out.print(BUILD_END);
		if (messages.stream().anyMatch(CompilerMessage::exception)) throw new Exception("Finished with exception");
	}

	private List<OutputItem> compile(CompilerConfiguration config, Map<File, Boolean> sources, List<CompilerMessage> messages, List<PostCompileActionMessage> postCompileActionMessages) {
		if (!sources.containsValue(false) && config.mode().equals(Mode.Build)) cleanOut(config);
		return new ArrayList<>(compileSources(config, sources, messages, postCompileActionMessages));
	}

	private List<OutputItem> compileSources(CompilerConfiguration config, Map<File, Boolean> sources, List<CompilerMessage> messages, List<PostCompileActionMessage> postCompileActionMessages) {
		List<OutputItem> outputItems = new KonosCompiler(config, messages, postCompileActionMessages).compile(new ArrayList<>(sources.keySet()));
		out.println();
		return outputItems;
	}

	private void report(Map<File, Boolean> srcFiles, List<OutputItem> compiled) {
		if (compiled.isEmpty()) reportNotCompiledItems(srcFiles);
		else reportCompiledItems(compiled, srcFiles);
		out.println();
	}

	private void processErrors(List<CompilerMessage> compilerMessages) {
		int errorCount = 0;
		for (CompilerMessage message : compilerMessages) {
			if (message.getCategory().equals(CompilerMessage.ERROR)) {
				if (errorCount > 100) continue;
				errorCount++;
			}
			printMessage(message);
		}
	}

	private void processActions(List<PostCompileActionMessage> postCompileActionMessages) {
		if (!postCompileActionMessages.isEmpty()) {
			out.print(START_ACTIONS_MESSAGE);
			postCompileActionMessages.forEach(this::printMessage);
			out.print(END_ACTIONS_MESSAGE);
		}
	}

	private void printMessage(CompilerMessage message) {
		out.print(MESSAGES_START);
		out.print(message.getCategoryLabel());
		out.print(SEPARATOR);
		out.print(message.getMessage());
		out.print(SEPARATOR);
		out.print(message.getUrl());
		out.print(SEPARATOR);
		out.print(message.getLineNum());
		out.print(SEPARATOR);
		out.print(message.getColumnNum());
		out.print(SEPARATOR);
		out.print(MESSAGES_END);
		out.println();
	}

	private void printMessage(PostCompileActionMessage message) {
		out.print(MESSAGE_ACTION_START);
		out.print(message.toString());
		out.print(MESSAGE_ACTION_END);
		out.println();
	}

	private void reportCompiledItems(List<OutputItem> compiledFiles, Map<File, Boolean> srcFiles) {
		for (OutputItem compiledFile : compiledFiles) {
			out.print(COMPILED_START);
			out.print(compiledFile.getOutputPath());
			out.print(SEPARATOR);
			out.print(new File(compiledFile.getSourcePath()).isFile() ? compiledFile.getSourcePath() : srcFiles.keySet().iterator().next().getAbsolutePath());
			out.print(COMPILED_END);
			out.println();
		}
	}

	private void reportNotCompiledItems(Map<File, Boolean> toRecompile) {
		for (File file : toRecompile.keySet()) {
			out.print(TO_RECOMPILE_START);
			out.print(file.getAbsolutePath());
			out.print(TO_RECOMPILE_END);
			out.println();
		}
	}

	public static void cleanOut(CompilerConfiguration configuration) {
		final String generationPackage = (configuration.generationPackage() == null ? configuration.module() : configuration.generationPackage()).replace(".", File.separator);
		File out = new File(configuration.genDirectory(), generationPackage.toLowerCase());
		if (out.exists()) try {
			FileUtils.deleteDirectory(out);
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}
}
