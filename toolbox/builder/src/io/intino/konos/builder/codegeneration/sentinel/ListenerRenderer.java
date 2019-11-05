package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.ActionTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Sentinel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class ListenerRenderer extends Renderer {
	private final List<Sentinel.SystemListener> systemSentinels;
	private final List<Sentinel> sentinels;

	public ListenerRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		this.systemSentinels = graph.sentinelList(Sentinel::isSystemListener).map(Sentinel::asSystemListener).collect(Collectors.toList());
		this.sentinels = graph.sentinelList().stream().filter(t -> !t.isSystemListener()).collect(Collectors.toList());
	}

	@Override
	public void render() {
		this.systemSentinels.forEach(this::processSystemSentinel);
		this.sentinels.stream().filter(Sentinel::isDirectoryListener).map(Sentinel::asDirectoryListener).forEach(this::processDirectorySentinel);
	}

	private void processSystemSentinel(Sentinel.SystemListener sentinel) {
		FrameBuilder builder = baseFrame("listener").add("name", sentinel.name$());
		List<Frame> targets = new ArrayList<>();
		targets.add(baseFrame(sentinel.name$()).add("name", sentinel.name$()).toFrame());
		builder.add("target", targets.toArray(new Frame[0]));
		writeFrame(destinyPackage(), sentinel.name$() + "Listener", template().render(builder.toFrame()));
		createCorrespondingAction(sentinel.a$(Sentinel.class));
	}

	private void processDirectorySentinel(Sentinel.DirectoryListener sentinel) {
		FrameBuilder frame = new FrameBuilder("action", "listener")
				.add("name", sentinel.name$())
				.add("box", boxName())
				.add("package", packageName())
				.add("parameter", parameters(sentinel));
		if (!alreadyRendered(src(), sentinel.a$(Sentinel.class)))
			writeFrame(actionsPackage(src()), sentinel.name$() + "Action", actionTemplate().render(frame));
	}

	private Frame[] parameters(Sentinel.DirectoryListener sentinel) {
		List<Frame> list = new ArrayList<>();
		list.add(new FrameBuilder("parameter").add("type", File.class.getCanonicalName()).add("name", "directory").toFrame());
		list.add(new FrameBuilder("parameter").add("type", "io.intino.alexandria.scheduler.directory.DirectorySentinel.Event").add("name", "event").toFrame());
		return list.toArray(new Frame[0]);
	}

	private void createCorrespondingAction(Sentinel sentinel) {
		if (!alreadyRendered(src(), sentinel))
			writeFrame(actionsPackage(src()), sentinel.name$() + "Action", actionTemplate().
					render(new FrameBuilder("action")
							.add("name", sentinel.name$())
							.add("box", boxName())
							.add("package", packageName()).toFrame()));
	}

	private Template actionTemplate() {
		return Formatters.customize(new ActionTemplate());
	}

	private Template template() {
		return Formatters.customize(new ListenerTemplate());
	}

	private boolean alreadyRendered(File destiny, Sentinel sentinel) {
		return Commons.javaFile(actionsPackage(destiny), sentinel.name$() + "Action").exists();
	}

	private File actionsPackage(File destiny) {
		return new File(destiny, "actions");
	}

	private File destinyPackage() {
		return new File(gen(), "scheduling");
	}

	@NotNull
	private FrameBuilder baseFrame(String... types) {
		return new FrameBuilder(types)
				.add("box", settings.boxName())
				.add("package", settings.packageName());
	}
}