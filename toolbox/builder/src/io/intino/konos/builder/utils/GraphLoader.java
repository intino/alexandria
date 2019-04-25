package io.intino.konos.builder.utils;

import com.intellij.compiler.CompilerMessageImpl;
import com.intellij.compiler.ProblemsView;
import com.intellij.compiler.impl.ProjectCompileScope;
import com.intellij.openapi.compiler.CompilerMessage;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.containers.ContainerUtil;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.StashBuilder;
import io.intino.tara.compiler.shared.TaraBuildConstants;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.loaders.ClassFinder;
import tara.dsl.Konos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.intino.tara.compiler.shared.TaraBuildConstants.*;
import static io.intino.tara.compiler.shared.TaraCompilerMessageCategories.ERROR;
import static io.intino.tara.compiler.shared.TaraCompilerMessageCategories.WARNING;

public class GraphLoader {
	private static final Logger LOG = Logger.getInstance("CreateKonosBoxAction: ");
	private static final Key<Object> PROBLEMS_VIEW_SESSION_ID_KEY = Key.create("ProblemsViewSessionKey");
	private static final Key<Object> PROBLEMS_VIEW_FILES_KEY = Key.create("ProblemsViewFiles");

	public KonosGraph loadGraph(Module module) {
		ClassFinder.clear();
		final Map<File, Charset> files = KonosUtils.findKonosFiles(module).stream().map(PsiFile::getVirtualFile).collect(Collectors.toMap(vf -> new File(vf.getPath()), VirtualFile::getCharset));
		if (!files.isEmpty()) {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final StashBuilder stashBuilder = new StashBuilder(files, new Konos(), module.getName(), new PrintStream(out));
			final Stash stash = stashBuilder.build();
			processMessages(module.getProject(), out);
			if (stash == null) return null;
			else return loadGraph(stash);
		} else return loadGraph();
	}

	private KonosGraph loadGraph(Stash... stashes) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = new Graph().loadStashes("Konos").loadStashes(stashes);
		if (graph == null) return null;
		final KonosGraph konosGraph = graph.as(KonosGraph.class);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return konosGraph;
	}

	private void processMessages(Project project, ByteArrayOutputStream stream) {
		try {
			final String messages = stream.toString("UTF-8");
			final CompilationMessageProcessor processor = new CompilationMessageProcessor(project);
			processor.process(messages);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private static class CompilationMessageProcessor {
		private final StringBuilder outputBuffer = new StringBuilder();
		private final List<CompilerMessage> compilerMessages = new ArrayList<>();
		private final Project project;

		public CompilationMessageProcessor(Project project) {
			this.project = project;
		}

		private void process(String output) {
			for (String line : output.split("\n")) parse(line);
			processMessages();
		}

		private void processMessages() {
			ProblemsView problemsView = ProblemsView.SERVICE.getInstance(project);
			final UUID id = getProblemsViewSessionId(project);
			problemsView.clearProgress();
			problemsView.clearOldMessages(new ProjectCompileScope(project), UUID.randomUUID());
			for (CompilerMessage compilerMessage : compilerMessages) problemsView.addMessage(compilerMessage, id);
		}

		private void parse(String output) {
			final String text = output.trim();
			if (StringUtil.isNotEmpty(text)) {
				if (text.startsWith(COMPILED_START)) outputBuffer.append(text);
				else if (text.startsWith(MESSAGES_START)) {
					outputBuffer.append(text);
					processMessage();
				}
				if (text.endsWith(COMPILED_END)) if (!text.startsWith(COMPILED_START)) outputBuffer.append(text);
			}
		}

		private void processMessage() {
			if (outputBuffer.indexOf(MESSAGES_END) == -1) return;
			String text = handleOutputBuffer(MESSAGES_START, MESSAGES_END);
			List<String> tokens = splitAndTrim(text);
			LOG.assertTrue(tokens.size() > 4, "Wrong number of output params");
			String category = tokens.get(0);
			String message = tokens.get(1);
			String url = tokens.get(2);
			String lineNum = tokens.get(3);
			String columnNum = tokens.get(4);
			int lineInt, columnInt;
			try {
				lineInt = Integer.parseInt(lineNum);
				columnInt = Integer.parseInt(columnNum);
			} catch (NumberFormatException e) {
				LOG.error(e);
				lineInt = 0;
				columnInt = 0;
			}
			CompilerMessageCategory kind = category.equals(ERROR)
					? CompilerMessageCategory.ERROR
					: category.equals(WARNING)
					? CompilerMessageCategory.WARNING
					: CompilerMessageCategory.INFORMATION;
			final VirtualFile file = fileOf(url);
			if (file == null) {
				LOG.error("File is null: " + url);
				return;
			}
			CompilerMessage compilerMessage = new CompilerMessageImpl(project, kind, message, file, lineInt, columnInt, PsiManager.getInstance(project).findFile(file));
			if (LOG.isDebugEnabled()) LOG.debug("Message: " + compilerMessage);
			compilerMessages.add(compilerMessage);
		}

		private VirtualFile fileOf(String url) {
			return VfsUtil.findFileByIoFile(new File(url), true);
		}

		private String handleOutputBuffer(String startMarker, String endMarker) {
			final int start = outputBuffer.indexOf(startMarker);
			final int end = outputBuffer.indexOf(endMarker);
			if (start > end)
				throw new AssertionError("Malformed Tarac output: " + outputBuffer.toString());
			String text = outputBuffer.substring(start + startMarker.length(), end);
			outputBuffer.delete(start, end + endMarker.length());
			return text.trim();
		}

		private List<String> splitAndTrim(String compiled) {
			return ContainerUtil.map(StringUtil.split(compiled, TaraBuildConstants.SEPARATOR), String::trim);
		}

		private static UUID getProblemsViewSessionId(Project project) {
			UUID problemsViewSessionId = (UUID) project.getUserData(PROBLEMS_VIEW_SESSION_ID_KEY);
			if (problemsViewSessionId == null) {
				problemsViewSessionId = UUID.randomUUID();
				project.putUserData(PROBLEMS_VIEW_SESSION_ID_KEY, problemsViewSessionId);
			}
			return problemsViewSessionId;
		}

	}
}
