package io.intino.konos;

import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.KonosCompiler;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.compiler.shared.KonosCompilerMessageCategories;
import io.intino.konos.compiler.shared.PostCompileActionMessage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static io.intino.konos.compiler.shared.KonosBuildConstants.*;


class KonosCompilerRunner {
	private static final Logger LOG = Logger.getGlobal();

	private final boolean verbose;
	private PrintStream out = System.out;

	KonosCompilerRunner(boolean verbose) {
		this.verbose = verbose;
	}

	boolean run(File argsFile) {
		final CompilerConfiguration config = new CompilerConfiguration();
		final Map<File, Boolean> sources = new LinkedHashMap<>();
		CompilationInfoExtractor.getInfoFromArgsFile(argsFile, config, sources);
		config.setVerbose(verbose);
		config.out(System.out);
		this.out = config.out();
		if (sources.isEmpty()) return true;
		if (verbose) out.println(PRESENTABLE_MESSAGE + "Konosc: loading sources...");
		final List<CompilerMessage> messages = new ArrayList<>();
		final List<PostCompileActionMessage> postCompileActionMessages = new ArrayList<>();
		List<OutputItem> compiled = compile(config, sources, messages, postCompileActionMessages);
		if (verbose) report(sources, compiled);
		processErrors(messages);
		if (messages.stream().noneMatch(m -> m.getCategory().equalsIgnoreCase(KonosCompilerMessageCategories.ERROR)))
			processActions(postCompileActionMessages);
		out.println();
		out.print(BUILD_END);
		return false;
	}

	private List<OutputItem> compile(CompilerConfiguration config, Map<File, Boolean> sources, List<CompilerMessage> messages, List<PostCompileActionMessage> postCompileActionMessages) {
		if (!sources.containsValue(false)) cleanOut(config);
		return new ArrayList<>(compileSources(config, sources, messages, postCompileActionMessages));
	}

	private List<OutputItem> compileSources(CompilerConfiguration config, Map<File, Boolean> sources, List<CompilerMessage> messages, List<PostCompileActionMessage> postCompileActionMessages) {
		List<OutputItem> outputItems = new KonosCompiler(config, messages, postCompileActionMessages).compile(new ArrayList<>(sources.keySet()));
		out.println();
		return outputItems;
	}

	private void report(Map<File, Boolean> srcFiles, List<OutputItem> compiled) {
		if (compiled.isEmpty()) reportNotCompiledItems(srcFiles);
		else reportCompiledItems(compiled);
		out.println();
	}

	public static void cleanOut(CompilerConfiguration configuration) {
		final String generationPackage = (configuration.generationPackage() == null ? configuration.module() : configuration.generationPackage()).replace(".", File.separator);
		File out = new File(configuration.outDirectory(), generationPackage.toLowerCase());
		if (out.exists()) try {
			FileUtils.deleteDirectory(out);
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}

	private void processErrors(List<CompilerMessage> compilerMessages) {
		int errorCount = 0;
		for (CompilerMessage message : compilerMessages) {
			if (message.getCategory().equals(KonosCompilerMessageCategories.ERROR)) {
				if (errorCount > 100) continue;
				errorCount++;
			}
			printMessage(message);
		}
	}

	private void processActions(List<PostCompileActionMessage> postCompileActionMessages) {
		for (PostCompileActionMessage message : postCompileActionMessages) {
			printMessage(message);
		}
	}

	private void printMessage(CompilerMessage message) {
		out.print(MESSAGES_START);
		out.print(message.getCategory());
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

	private void reportCompiledItems(List<OutputItem> compiledFiles) {
		for (OutputItem compiledFile : compiledFiles) {
			out.print(COMPILED_START);
			out.print(compiledFile.getOutputPath());
			out.print(SEPARATOR);
			out.print(compiledFile.getSourceFile());
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
}
