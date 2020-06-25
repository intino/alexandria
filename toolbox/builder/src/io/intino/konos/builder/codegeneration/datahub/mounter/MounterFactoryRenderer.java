package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.CompilationContext.DataHubManifest;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.model.graph.Datamart;
import io.intino.konos.model.graph.Datamart.Mounter;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterFactoryRenderer {
	private final CompilationContext context;
	private final List<Mounter> mounters;
	private final File genMounters;

	public MounterFactoryRenderer(CompilationContext context, KonosGraph graph) {
		this.context = context;
		this.mounters = graph.datamartList().stream().map(Datamart::mounterList).flatMap(Collection::stream).collect(Collectors.toList());
		this.genMounters = new File(context.gen(Target.Owner), "mounters");
	}

	public void execute() throws KonosException {
		if (mounters.isEmpty()) return;
		DataHubManifest manifest = context.dataHubManifest();
		if (manifest == null)
			throw new KonosException("Is required the Data hub declaration in artifact to instance subscribers");
		FrameBuilder builder = baseFrame("factory");
		Map<String, List<Mounter>> map = map(mounters, manifest);
		for (String event : map.keySet())
			builder.add("event", baseFrame("event").
					add("type", event.substring(event.lastIndexOf(".") + 1)).
					add("mounter", map.get(event).stream().map(m -> baseFrame("mounter").add("datamart", m.core$().owner().name()).add("name", m.name$()).toFrame()).toArray(Frame[]::new)));
		context.classes().put(Mounter.class.getSimpleName() + "#" + "MounterFactory", "mounters.mounterFactory");
		Frame object = builder.toFrame();
		writeFrame(genMounters, "MounterFactory", customize(new MounterFactoryTemplate()).render(object));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(mounters.get(0)), javaFile(genMounters, "MounterFactory").getAbsolutePath()));
		writeFrame(genMounters, "Mounter", customize(new IMounterTemplate()).render(object));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(mounters.get(0)), javaFile(genMounters, "Mounter").getAbsolutePath()));
	}

	private Map<String, List<Mounter>> map(List<Mounter> mounters, DataHubManifest manifest) {
		Map<String, List<Mounter>> mountersByRequire = new HashMap<>();
		for (Mounter mounter : mounters)
			mounter.asEvent().requireList().forEach(r -> {
				String key = manifest.tankClasses.get(r.tank());
				if (key == null) return;
				if (!mountersByRequire.containsKey(key)) mountersByRequire.put(key, new ArrayList<>());
				mountersByRequire.get(key).add(mounter);
			});
		return mountersByRequire;
	}

	private FrameBuilder baseFrame(String name) {
		return new FrameBuilder(name).
				add("box", context.boxName()).
				add("package", context.packageName());
	}
}