package io.intino.konos.builder.codegeneration.datahub.adapter;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Adapter;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AdapterRenderer {
	private final CompilationContext context;
	private final List<Adapter> adapters;

	public AdapterRenderer(CompilationContext context, KonosGraph graph) {
		this.context = context;
		this.adapters = graph.adapterList();
	}

	public void execute() {
		for (Adapter adapter : adapters) {
			final FrameBuilder builder = new FrameBuilder("adapter").
					add("box", context.boxName()).
					add("package", context.packageName()).
					add("name", adapter.name$());
			final File directory = new File(context.src(Target.Owner), "adapters");
			final String adapterName = adapter.name$();
			context.classes().put(adapter.getClass().getSimpleName() + "#" + adapter.name$(), "adapters." + adapterName);
			if (!alreadyRendered(directory, adapterName)) {
				writeFrame(directory, adapterName, customize(new AdapterTemplate()).render(builder.toFrame()));
				context.compiledFiles().add(new OutputItem(context.sourceFileOf(adapter), javaFile(directory, adapterName).getAbsolutePath()));
			}
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}
