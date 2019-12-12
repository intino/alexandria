package io.intino.konos.builder.codegeneration.datahub.subscriber;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Subscriber;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class SubscriberRenderer {
	private final Settings settings;
	private final List<Subscriber> subscribers;
	private final File srcSubscribers;

	public SubscriberRenderer(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.subscribers = graph.subscriberList();
		this.srcSubscribers = new File(settings.src(Target.Owner), "subscribers");
	}

	public void execute() {
		for (Subscriber subscriber : subscribers) {
			final FrameBuilder builder = baseFrame(subscriber);
			Settings.DataHubManifest manifest = settings.dataHubManifest();
			if (manifest == null) return;
			String type = manifest.tankClasses.get(subscriber.tank());
			if (type == null) return;
			builder.add("type", type);
			builder.add("typeName", type.substring(type.lastIndexOf(".") + 1));
			settings.classes().put(subscriber.getClass().getSimpleName() + "#" + subscriber.name$(), "subscribers." + subscriber.name$());
			if (!alreadyRendered(srcSubscribers, subscriber.name$()))
				writeFrame(srcSubscribers, subscriber.name$(), customize(new SubscriberTemplate()).render(builder.toFrame()));
		}
	}

	@NotNull
	private FrameBuilder baseFrame(Subscriber subscriber) {
		return new FrameBuilder("subscriber").
				add("box", settings.boxName()).
				add("package", settings.packageName()).
				add("name", subscriber.name$());
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}