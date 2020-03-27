package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.WebHookActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Sentinel;

import java.util.ArrayList;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static java.util.stream.Collectors.toList;

public class SentinelsRenderer extends Renderer {
	private final List<Sentinel> sentinels;

	public SentinelsRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.sentinels = graph.sentinelList();
	}

	@Override
	public void render() {
		if (sentinels.isEmpty()) return;
		Commons.writeFrame(gen(), "Sentinels", template().render(
				new FrameBuilder("Sentinels")
						.add("package", packageName())
						.add("box", boxName())
						.add("sentinel", processSentinels()).toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(sentinels.get(0)), javaFile(gen(), "Sentinels").getAbsolutePath()));
	}

	private Frame[] processSentinels() {
		List<Frame> list = new ArrayList<>();
		list.addAll(sentinels.stream().filter(t -> t.i$(Sentinel.SystemListener.class)).map(t -> t.a$(Sentinel.SystemListener.class)).map(this::processSentinel).collect(toList()));
		list.addAll(sentinels.stream().filter(t -> t.i$(Sentinel.DirectoryListener.class)).map(t -> t.a$(Sentinel.DirectoryListener.class)).map(this::processDirectoryListenerSentinel).collect(toList()));
		list.addAll(sentinels.stream().filter(t -> t.i$(Sentinel.WebHook.class)).map(t -> t.a$(Sentinel.WebHook.class)).map(this::processWebHookSentinel).collect(toList()));
		sentinels.stream().filter(t -> t.i$(Sentinel.WebHook.class)).forEach(s -> new WebHookActionRenderer(context, s.asWebHook()).execute());
		return list.toArray(new Frame[0]);
	}

	private Frame processWebHookSentinel(Sentinel.WebHook webhook) {
		final FrameBuilder builder = new FrameBuilder().add("sentinel").add(webhook.getClass().getSimpleName()).add("name", webhook.name$());
		builder.add("path", customize("path", ("/webhook/" + webhook.path()).replace("//", "")));
		return builder.toFrame();
	}

	private Frame processSentinel(Sentinel.SystemListener task) {
		final FrameBuilder builder = new FrameBuilder().add("sentinel").add(task.getClass().getSimpleName()).add("name", task.name$());
		List<Frame> jobFrames = new ArrayList<>();
		if (task.i$(Sentinel.ClockListener.class)) {
			Sentinel.ClockListener cron = task.a$(Sentinel.ClockListener.class);
			FrameBuilder jobFrameBuilder = new FrameBuilder().add("job").add("Cron" + task.getClass().getSimpleName())
					.add("name", task.core$().id());
			jobFrameBuilder.add("cronTrigger")
					.add("pattern", cron.pattern())
					.add("mean", cron.mean());
			if (cron.timeZone() != null) jobFrameBuilder.add("timeZone", cron.timeZone());
			jobFrames.add(jobFrameBuilder.toFrame());
		}
		if (task.i$(Sentinel.BootListener.class)) {
			final FrameBuilder jobFrameBuilder = new FrameBuilder("onBootTrigger", "job", "Boot" + task.getClass().getSimpleName())
					.add("name", task.core$().id());
			jobFrames.add(jobFrameBuilder.toFrame());
		}
		builder.add("job", jobFrames.toArray(new Frame[0]));
		return builder.toFrame();
	}

	private Frame processDirectoryListenerSentinel(Sentinel.DirectoryListener sentinel) {
		final FrameBuilder builder = new FrameBuilder().add("sentinel").add(sentinel.getClass().getSimpleName())
				.add("event", sentinel.events().stream().map(Enum::name).toArray(String[]::new))
				.add("file", sentinel.directories() == null ? "" : sentinel.directories().get(0))
				.add("name", sentinel.name$())
				.add("package", packageName());
		return builder.toFrame();
	}

	private Template template() {
		return Formatters.customize(new SentinelsTemplate());
	}
}
