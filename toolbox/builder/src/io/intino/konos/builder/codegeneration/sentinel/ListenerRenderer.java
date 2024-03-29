package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.action.ActionTemplate;
import io.intino.konos.builder.codegeneration.action.ActionUpdater;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Sentinel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class ListenerRenderer extends Renderer {
	private final List<Sentinel.SystemListener> systemSentinels;
	private final List<Sentinel> sentinels;

	public ListenerRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.systemSentinels = graph.sentinelList(Sentinel::isSystemListener).map(Sentinel::asSystemListener).collect(Collectors.toList());
		this.sentinels = graph.sentinelList().stream().filter(t -> !t.isSystemListener()).collect(Collectors.toList());
	}

	@Override
	public void render() {
		this.systemSentinels.forEach(this::processSystemSentinel);
		this.sentinels.stream().filter(Sentinel::isFileListener).map(Sentinel::asFileListener).forEach(this::processFileListener);
	}

	private void processSystemSentinel(Sentinel.SystemListener sentinel) {
		FrameBuilder builder = baseFrame("listener").add("name", sentinel.name$());
		List<Frame> targets = new ArrayList<>();
		targets.add(baseFrame(sentinel.name$()).add("name", sentinel.name$()).toFrame());
		builder.add("target", targets.toArray(new Frame[0]));
		writeFrame(destinyPackage(), sentinel.name$() + "Listener", template().render(builder.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(sentinel), javaFile(destinyPackage(), sentinel.name$() + "Listener").getAbsolutePath()));
		createCorrespondingAction(sentinel.a$(Sentinel.class));
	}

	private void processFileListener(Sentinel.FileListener sentinel) {
		FrameBuilder frame = new FrameBuilder("action", "listener")
				.add("name", sentinel.name$())
				.add("box", boxName())
				.add("package", packageName())
				.add("parameter", parameters());
		if (!alreadyRendered(src(Target.Server), sentinel.a$(Sentinel.class)))
			writeFrame(actionsPackage(src(Target.Server)), sentinel.name$() + "Action", actionTemplate().render(frame));
	}

	private Frame[] parameters() {
		List<Frame> list = new ArrayList<>();
		list.add(new FrameBuilder("parameter").add("type", File.class.getCanonicalName()).add("name", "file").toFrame());
		list.add(new FrameBuilder("parameter").add("type", "io.intino.alexandria.scheduler.directory.DirectorySentinel.Event").add("name", "event").toFrame());
		return list.toArray(new Frame[0]);
	}

	private void createCorrespondingAction(Sentinel sentinel) {
		if (!alreadyRendered(src(Target.Server), sentinel)) {
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(sentinel), javaFile(actionsPackage(src(Target.Server)), sentinel.name$() + "Action").getAbsolutePath()));
			writeFrame(actionsPackage(src(Target.Server)), sentinel.name$() + "Action", actionTemplate().
					render(new FrameBuilder("action")
							.add("name", sentinel.name$())
							.add("box", boxName())
							.add("package", packageName()).toFrame()));
		} else
			new ActionUpdater(context, javaFile(actionsPackage(src(Target.Server)), sentinel.name$() + "Action"), "actions", Collections.emptyMap(), Collections.emptyList(), null).update();
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
		return new File(gen(Target.Server), "scheduling");
	}


	private FrameBuilder baseFrame(String... types) {
		return new FrameBuilder(types)
				.add("box", context.boxName())
				.add("package", context.packageName());
	}
}