package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.MessageHub;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MessageHubRenderer {
	private final Settings settings;
	private final KonosGraph graph;
	private final File sourceDirectory;
	private final File genDirectory;

	public MessageHubRenderer(Settings settings, KonosGraph graph) {
		this.graph = graph;
		this.settings = settings;
		this.sourceDirectory = settings.src(Target.Owner);
		this.genDirectory = settings.gen(Target.Owner);
	}

	public void execute() {
		MessageHub terminal = graph.messageHub();
		if (terminal == null) return;
		final FrameBuilder builder = new FrameBuilder("messageHub").
				add("box", settings.boxName()).
				add("package", settings.packageName());
		if (terminal.isJmsBus()) builder.add("jms");
		settings.classes().put("MessageHub", "MessageHub");
		File destination = terminal.isJmsBus() ? genDirectory : sourceDirectory;
		if (!Commons.javaFile(destination, "MessageHub").exists())
			writeFrame(destination, "MessageHub", customize(new MessageHubTemplate()).render(builder.toFrame()));
	}
}
