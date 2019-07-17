package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.DataHub;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.MessageHub;

import java.io.File;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MessageHubRenderer {
	private final KonosGraph graph;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;
	private final File sourceDirectory;
	private final File genDirectory;

	public MessageHubRenderer(KonosGraph graph, File gen, File src, String packageName, String boxName, Map<String, String> classes) {
		this.graph = graph;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
		this.sourceDirectory = new File(src, "datahub");
		this.genDirectory = new File(gen, "datahub");
	}

	public void execute() {
		MessageHub messageHub = messageHub();
		if (messageHub == null) return;
		final FrameBuilder builder = new FrameBuilder("messageHub").
				add("box", boxName).
				add("package", packageName);
		if (messageHub.isJmsHub()) builder.add("jms");
		for (DataHub.Tank tank : graph.dataHub().tankList((r) -> true))
			builder.add("tank", new FrameBuilder().add("name", tank.fullName().replace(".", "_")).add("qn", tank.fullName()));
		classes.put("MessageHub", "datahub.MessageHub");
		File destination = messageHub.isJmsHub() ? genDirectory : sourceDirectory;
		if (!Commons.javaFile(destination, "MessageHub").exists())
			writeFrame(destination, "MessageHub", customize(new MessageHubTemplate()).render(builder.toFrame()));
	}

	private MessageHub messageHub() {
		if (graph.dataHub() == null) return null;
		if (graph.dataHub().isMirrored()) return graph.dataHub().asMirrored().messageHub();
		if (graph.dataHub().isRemote()) return graph.dataHub().asRemote().messageHub();
		return null;
	}
}
