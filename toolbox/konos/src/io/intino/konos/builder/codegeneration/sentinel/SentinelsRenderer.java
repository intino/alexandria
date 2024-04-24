package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.action.WebHookActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Sentinel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static java.util.stream.Collectors.toList;

public class SentinelsRenderer extends Renderer {
	private final List<Sentinel> sentinels;

	public SentinelsRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.sentinels = graph.sentinelList();
	}

	@Override
	public void render() throws KonosException {
		if (sentinels.isEmpty()) return;
		FrameBuilder builder = new FrameBuilder("Sentinels")
				.add("package", packageName())
				.add("box", boxName())
				.add("sentinel", processSentinels());
		if (sentinels.stream().anyMatch(Sentinel::isWebHook)) builder.add("hasWebhook", ",");
		Commons.writeFrame(gen(Target.Server), "Sentinels", template().render(
				builder.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(sentinels.get(0)), javaFile(gen(Target.Server), "Sentinels").getAbsolutePath()));
	}

	private Frame[] processSentinels() throws KonosException {
		List<Frame> list = new ArrayList<>();
		list.addAll(sentinels.stream().filter(t -> t.i$(Sentinel.SystemListener.class)).map(t -> t.a$(Sentinel.SystemListener.class)).map(this::processSentinel).collect(toList()));
		list.addAll(fileListeners());
		list.addAll(sentinels.stream().filter(t -> t.i$(Sentinel.WebHook.class)).map(t -> t.a$(Sentinel.WebHook.class)).map(this::processWebHookSentinel).collect(toList()));
		for (Sentinel t : sentinels)
			if (t.i$(Sentinel.WebHook.class)) new WebHookActionRenderer(context, t.asWebHook()).execute();
		return list.toArray(new Frame[0]);
	}

	private List<Frame> fileListeners() throws KonosException {
		List<Frame> list = new ArrayList<>();
		for (Sentinel t : sentinels) {
			if (t.i$(Sentinel.FileListener.class)) {
				Sentinel.FileListener fileListener = t.a$(Sentinel.FileListener.class);
				Frame frame = processFileListenerSentinel(fileListener);
				list.add(frame);
			}
		}
		return list;
	}

	private Frame processWebHookSentinel(Sentinel.WebHook webHook) {
		final FrameBuilder builder = new FrameBuilder("sentinel").add(webHook.getClass().getSimpleName()).add("name", webHook.name$());
		builder.add("path", customize("path", ("/webhook/" + webHook.path()).replace("//", "/")));
		builder.add("parameter", webHook.parameterList().stream().map(p -> new FrameBuilder("parameter").add("name", p.name$()).add("in", p.in().name()).toFrame()).toArray(Frame[]::new));
		return builder.toFrame();
	}

	private Frame processSentinel(Sentinel.SystemListener task) {
		final FrameBuilder builder = new FrameBuilder().add("sentinel").add(task.getClass().getSimpleName()).add("name", task.name$());
		builder.add("package", packageName());
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
			final FrameBuilder fb = new FrameBuilder("onBootTrigger", "job", "Boot" + task.getClass().getSimpleName())
					.add("name", task.core$().id());
			if (task.asSentinel().asBootListener().delay() > 0)
				fb.add("delay").add("delay", task.asSentinel().asBootListener().delay());
			jobFrames.add(fb.toFrame());
		}
		builder.add("job", jobFrames.toArray(new Frame[0]));
		return builder.toFrame();
	}

	private Frame processFileListenerSentinel(Sentinel.FileListener sentinel) throws KonosException {
		Set<String> custom = Commons.extractParameters(sentinel.file());
		Frame fileFrame = Commons.fileFrame(sentinel.file(), packageName(), context.archetypeQN());
		final FrameBuilder builder = new FrameBuilder().add("sentinel").add(sentinel.getClass().getSimpleName())
				.add("event", sentinel.events().stream().map(Enum::name).toArray(String[]::new))
				.add("name", sentinel.name$())
				.add("package", packageName());
		if (custom.isEmpty())
			builder.add("file", Commons.fileFrame(sentinel.file(), packageName(), context.archetypeQN()));
		else builder.add("file", new FrameBuilder("custom", "file").add("path", custom.iterator().next()));
		return builder.toFrame();
	}

	private Template template() {
		return Formatters.customize(new SentinelsTemplate());
	}
}
