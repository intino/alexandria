package io.intino.konos.builder.codegeneration.messagehub;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.MessageHub;
import io.intino.konos.model.graph.realtime.RealtimeMounter;
import io.intino.konos.model.graph.realtime.RealtimeMounter.Source;

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
		MessageHub messageHub = graph.messageHub();
		if (messageHub == null) return;
		final FrameBuilder builder = new FrameBuilder("messageHub").
				add("box", settings.boxName()).
				add("package", settings.packageName());
		if (messageHub.isJmsBus()) builder.add("jms");
		graph.realtimeMounterList().forEach(mounter -> builder.add("mounter", frameOf(mounter)));
		settings.classes().put("MessageHub", "MessageHub");
		File destination = messageHub.isJmsBus() ? genDirectory : sourceDirectory;
		if (!Commons.javaFile(destination, "MessageHub").exists())
			writeFrame(destination, "MessageHub", customize(new MessageHubTemplate()).render(builder.toFrame()));
	}

	private FrameBuilder frameOf(RealtimeMounter mounter) {
		return new FrameBuilder("mounter").add("package", settings.packageName()).add("name", mounter.name$()).add("source", mounter.sourceList().stream().map(Source::channel).toArray(String[]::new));
	}

}
