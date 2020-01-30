package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.model.graph.Datamart;
import io.intino.konos.model.graph.Datamart.Mounter;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterFactoryRenderer {
	private final CompilationContext compilationContext;
	private final List<Mounter> mounters;
	private File genMounters;

	public MounterFactoryRenderer(CompilationContext compilationContext, KonosGraph graph) {
		this.compilationContext = compilationContext;
		this.mounters = graph.datamartList().stream().map(Datamart::mounterList).flatMap(Collection::stream).collect(Collectors.toList());
		this.genMounters = new File(compilationContext.gen(Target.Owner), "mounters");
	}

	public void execute() {
		if (mounters.isEmpty()) return;
		FrameBuilder builder = baseFrame("factory");
		for (Mounter mounter : mounters) builder.add("mounter", baseFrame("mounter").add("name", mounter.name$()));
		compilationContext.classes().put(Mounter.class.getSimpleName() + "#" + "MounterFactory", "mounters.mounterFactory");
		Frame object = builder.toFrame();
		writeFrame(genMounters, "MounterFactory", customize(new MounterFactoryTemplate()).render(object));
		writeFrame(genMounters, "Mounter", customize(new IMounterTemplate()).render(object));
	}

	private FrameBuilder baseFrame(String name) {
		return new FrameBuilder(name).
				add("box", compilationContext.boxName()).
				add("package", compilationContext.packageName());
	}
}