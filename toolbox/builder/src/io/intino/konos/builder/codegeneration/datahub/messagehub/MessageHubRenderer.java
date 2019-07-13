package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.MessageHub;

import java.io.File;
import java.util.Map;

public class MessageHubRenderer {


	private final KonosGraph graph;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;
	private final File sourceDirectory;

	public MessageHubRenderer(KonosGraph graph, File src, String packageName, String boxName, Map<String, String> classes) {
		this.graph = graph;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
		this.sourceDirectory = new File(src, "datahub");
	}

	public void execute() {
		MessageHub messageHub = messageHub();
		if (messageHub == null) return;
		final FrameBuilder builder = new FrameBuilder("messageHub").
				add("box", boxName).
				add("package", packageName);
		if (!alreadyRendered(sourceDirectory, "MessageHub")) {

		}
		classes.put("MessageHub", "datahub.MessageHub");
	}

}

	private MessageHub messageHub() {
		if (graph.dataHub() == null) return null;
		if (graph.dataHub().isMirrored()) return graph.dataHub().asMirrored().messageHub();
		if (graph.dataHub().isRemote()) return graph.dataHub().asRemote().messageHub();
		return null;
	}

	private boolean isCustom(String value) {
		final boolean custom = value != null && value.startsWith("{");
		if (custom) customParameters.add(value.substring(1, value.length() - 1));
		return custom;
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}


}
