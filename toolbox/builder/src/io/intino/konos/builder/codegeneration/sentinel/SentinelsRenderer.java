package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Sentinel;
import io.intino.konos.model.graph.bootlistener.BootListenerSentinel;
import io.intino.konos.model.graph.clocklistener.ClockListenerSentinel;
import io.intino.konos.model.graph.directorylistener.DirectoryListenerSentinel;
import io.intino.konos.model.graph.systemlistener.SystemListenerSentinel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SentinelsRenderer extends Renderer {
	private final List<Sentinel> sentinels;

	public SentinelsRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
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
	}

	private Frame[] processSentinels() {
		List<Frame> list = new ArrayList<>();
		list.addAll(sentinels.stream().filter(t -> t.i$(SystemListenerSentinel.class)).map(t -> t.a$(SystemListenerSentinel.class)).map(this::processSentinel).collect(Collectors.toList()));
		list.addAll(sentinels.stream().filter(t -> t.i$(DirectoryListenerSentinel.class)).map(t -> t.a$(DirectoryListenerSentinel.class)).map(this::processDirectoryListenerSentinel).collect(Collectors.toList()));
		return list.toArray(new Frame[0]);
	}

	private Frame processSentinel(SystemListenerSentinel task) {
		final FrameBuilder builder = new FrameBuilder().add("sentinel").add(task.getClass().getSimpleName()).add("name", task.name$());
		List<Frame> jobFrames = new ArrayList<>();
		if (task.i$(ClockListenerSentinel.class)) {
			ClockListenerSentinel cron = task.a$(ClockListenerSentinel.class);
			FrameBuilder jobFrameBuilder = new FrameBuilder().add("job").add("Cron" + task.getClass().getSimpleName())
					.add("name", task.core$().id());
			jobFrameBuilder.add("cronTrigger")
					.add("pattern", cron.pattern())
					.add("mean", cron.mean());
			if (cron.timeZone() != null) jobFrameBuilder.add("timeZone", cron.timeZone());
			jobFrames.add(jobFrameBuilder.toFrame());
		}
		if (task.i$(BootListenerSentinel.class)) {
			final FrameBuilder jobFrameBuilder = new FrameBuilder("onBootTrigger", "job", "Boot" + task.getClass().getSimpleName())
					.add("name", task.core$().id());
			jobFrames.add(jobFrameBuilder.toFrame());
		}
		builder.add("job", jobFrames.toArray(new Frame[0]));
		return builder.toFrame();
	}

	private Frame processDirectoryListenerSentinel(DirectoryListenerSentinel sentinel) {
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
