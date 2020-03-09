package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.MessageHub;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MessageHubRenderer {
	private final CompilationContext context;
	private final KonosGraph graph;
	private final File sourceDirectory;
	private final File genDirectory;

	public MessageHubRenderer(CompilationContext context, KonosGraph graph) {
		this.graph = graph;
		this.context = context;
		this.sourceDirectory = context.src(Target.Owner);
		this.genDirectory = context.gen(Target.Owner);
	}

	public void execute() {
		MessageHub terminal = graph.messageHub();
		if (terminal == null) return;
		final FrameBuilder builder = new FrameBuilder("messageHub").
				add("box", context.boxName()).
				add("package", context.packageName());
		if (terminal.isJmsBus()) builder.add("jms");
		context.classes().put("MessageHub", "MessageHub");
		File destination = terminal.isJmsBus() ? genDirectory : sourceDirectory;
		if (!Commons.javaFile(destination, "MessageHub").exists()) {
			writeFrame(destination, "MessageHub", customize(new MessageHubTemplate()).render(builder.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(terminal), javaFile(destination, "MessageHub").getAbsolutePath()));
		}
	}
}
