package io.intino.konos.builder.codegeneration.datalake.mounter;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Mounter;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterRenderer extends Renderer {
	private final List<Mounter> mounters;

	public MounterRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Service);
		this.mounters = graph.nessClient(0).mounterList();
	}

	@Override
	public void render() {
		final String packageName = packageName();

		for (Mounter mounter : mounters) {
			final FrameBuilder builder = new FrameBuilder("mounter").
					add("box", boxName()).
					add("package", packageName).
					add("name", mounter.name());
			if (mounter.schema() != null) {
				builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName));
				builder.add("type", new FrameBuilder("schema").add("package", packageName).add("name", mounter.schema().name$()));
			} else builder.add("type", "message");
			final File destination = new File(src(), "datalake/mounters");
			final String handlerName = mounter.name() + "Mounter";
			classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "datalake.mounters." + handlerName);
			if (!alreadyRendered(destination, handlerName))
				writeFrame(destination, handlerName, customize(new MounterTemplate()).render(builder.toFrame()));
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}