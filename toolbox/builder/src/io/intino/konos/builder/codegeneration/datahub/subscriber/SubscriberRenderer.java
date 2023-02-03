package io.intino.konos.builder.codegeneration.datahub.subscriber;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.context.WarningMessage;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Subscriber;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class SubscriberRenderer {
	private final CompilationContext context;
	private final List<Subscriber> subscribers;
	private final File srcSubscribers;

	public SubscriberRenderer(CompilationContext context, KonosGraph graph) {
		this.context = context;
		this.subscribers = graph.subscriberList();
		this.srcSubscribers = new File(context.src(Target.Server), "subscribers");
	}

	public void execute() throws KonosException {
		CompilationContext.DataHubManifest manifest = context.dataHubManifest();
		if (!subscribers.isEmpty() && manifest == null)
			throw new KonosException("Required Data hub declaration in artifact to instance subscribers");
		if (manifest == null) return;
		for (Subscriber subscriber : subscribers) {
			final FrameBuilder builder = baseFrame(subscriber);
			String type = manifest.tankClasses.get(subscriber.event());
			if (type == null) {
				context.addWarning(new WarningMessage("Tank " + subscriber.event() + " not found. Available: " + String.join("; ", manifest.tankClasses.keySet()), null, 1, 1));
				continue;
			}
			builder.add("type", type);
			builder.add("typeName", type.substring(type.lastIndexOf(".") + 1));
			context.classes().put(subscriber.getClass().getSimpleName() + "#" + subscriber.name$(), "subscribers." + subscriber.name$());
			if (!alreadyRendered(srcSubscribers, subscriber.name$())) {
				writeFrame(srcSubscribers, subscriber.name$(), customize(new SubscriberTemplate()).render(builder.toFrame()));
				context.compiledFiles().add(new OutputItem(context.sourceFileOf(subscriber), javaFile(srcSubscribers, subscriber.name$()).getAbsolutePath()));
			}
		}
	}

	private FrameBuilder baseFrame(Subscriber subscriber) {
		return new FrameBuilder("subscriber").
				add("box", context.boxName()).
				add("package", context.packageName()).
				add("name", subscriber.name$());
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}