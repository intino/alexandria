package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.DataHub;
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
		this.sourceDirectory = new File(settings.src(Target.Owner), "datahub");
		this.genDirectory = new File(settings.gen(Target.Owner), "datahub");
	}

	public void execute() {
		MessageHub messageHub = messageHub();
		if (messageHub == null) return;
		final FrameBuilder builder = new FrameBuilder("messageHub").
				add("box", settings.boxName()).
				add("package", settings.packageName());
		if (messageHub.isJmsHub()) builder.add("jms");
		for (DataHub.Tank tank : graph.dataHub().tankList((r) -> true))
			builder.add("tank", new FrameBuilder().add("name", tank.fullName().replace(".", "_")).add("qn", tank.fullName()));
		settings.classes().put("MessageHub", "datahub.MessageHub");
		File destination = messageHub.isJmsHub() ? genDirectory : sourceDirectory;
		if (!Commons.javaFile(destination, "MessageHub").exists())
			writeFrame(destination, "MessageHub", customize(new MessageHubTemplate()).render(builder.toFrame()));
	}

	private MessageHub messageHub() {
		if (graph.dataHub() == null) return null;
		if (graph.dataHub().isMirrored()) return graph.dataHub().asMirrored().messageHub();
		if (graph.dataHub().isLocal()) return graph.dataHub().asLocal().messageHub();
		return null;
	}
}
