package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Datamart;
import io.intino.konos.model.Datamart.Mounter;
import io.intino.konos.model.KonosGraph;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterRenderer {
	private final CompilationContext context;
	private final KonosGraph graph;
	private final File genMounters;

	public MounterRenderer(CompilationContext context, KonosGraph graph) {
		this.context = context;
		this.graph = graph;
		this.genMounters = new File(context.gen(Target.Server), "mounters");
	}

	public void execute() {
		CompilationContext.DataHubManifest manifest = context.dataHubManifest();
		for (Datamart datamart : graph.datamartList())
			for (Mounter mounter : datamart.mounterList()) {
				final String mounterName = mounter.name$();
				final FrameBuilder builder = baseFrame(mounter);
				if (mounter.isEvent() && manifest != null && !alreadyRendered(context.src(Target.Server), mounterName))
					eventMounter(mounter, mounterName, manifest, builder);
			}
	}

	private FrameBuilder baseFrame(Mounter mounter) {
		return new FrameBuilder("mounter").
				add("box", context.boxName()).
				add("package", context.packageName()).
				add("name", mounter.name$());
	}

	private void eventMounter(Mounter mounter, String mounterName, CompilationContext.DataHubManifest manifest, FrameBuilder builder) {
		String datamart = mounter.core$().ownerAs(Datamart.class).name$();
		builder.add("event").add("datamart", datamart).add("type", types(mounter, manifest));
		context.classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "mounters." + datamart + "." + mounterName);
		File datamartFolder = new File(context.src(Target.Server), datamart);
		File mounters = new File(datamartFolder, "mounters");
		if (!alreadyRendered(mounters, mounterName)) {
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(mounter), javaFile(mounters, firstUpperCase(mounterName)).getAbsolutePath()));
			writeFrame(mounters, mounterName, customize(new MounterTemplate()).render(builder.toFrame()));
		}
	}

	private Frame[] types(Mounter mounter, CompilationContext.DataHubManifest manifest) {
		return mounter.asEvent().requireList().stream()
				.filter(r -> manifest.tankClasses.containsKey(r.tank()))
				.map(r -> frameOf(manifest, r))
				.toArray(Frame[]::new);
	}

	private static Frame frameOf(CompilationContext.DataHubManifest manifest, Mounter.Event.Require r) {
		return new FrameBuilder()
				.add("fullType", manifest.tankClasses.get(r.tank()))
				.add("name", name(r.tank()))
				.toFrame();
	}

	private static String name(String tank) {
		return tank.contains(".") ? tank.substring(tank.lastIndexOf(".") + 1) : tank;
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}