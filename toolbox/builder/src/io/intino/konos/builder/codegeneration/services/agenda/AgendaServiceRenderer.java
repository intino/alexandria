package io.intino.konos.builder.codegeneration.services.agenda;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Data;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Parameter;
import io.intino.konos.model.Service.Agenda;

import java.util.List;
import java.util.stream.IntStream;

import static io.intino.konos.builder.helpers.Commons.javaFile;

public class AgendaServiceRenderer extends Renderer {
	private final List<Agenda> agendas;

	public AgendaServiceRenderer(CompilationContext context, KonosGraph graph) {
		super(context);
		agendas = graph.agendaServiceList();
	}

	@Override
	protected void render() throws KonosException {
		if (!agendas.isEmpty()) render(agendas.get(0));
	}

	private void render(Agenda agenda) throws KonosException {
		FrameBuilder builder = new FrameBuilder("agenda", "service")
				.add("package", packageName())
				.add("box", boxName())
				.add("rootPath", Commons.fileFrame(agenda.filePath(), packageName(), context.archetypeQN()))
				.add("baseUri", agenda.baseUri())
				.add("future", processFutures(agenda.futureList()));
		if (!agenda.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		Commons.writeFrame(gen(Target.Server), "AgendaService", template().render(builder.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(agenda), javaFile(gen(Target.Server), "AgendaService").getAbsolutePath()));
		for (Agenda.Future future : agenda.futureList()) new FutureRenderer(context, future).render();
	}

	private Frame[] processFutures(List<Agenda.Future> futureList) {
		return futureList.stream().map(this::frameOf).map(FrameBuilder::toFrame).toArray(Frame[]::new);
	}

	private FrameBuilder frameOf(Agenda.Future f) {
		FrameBuilder builder = new FrameBuilder("future")
				.add("name", f.name$())
				.add("parameter", framesOf(f.parameterList()))
				.add("option", f.optionList().stream().map(option -> frameOf(option, framesOf(f.parameterList()))).toArray(Frame[]::new));
		if (!f.optionList().isEmpty()) builder.add("hasOption", "true");
		return builder;
	}

	private Frame frameOf(Agenda.Future.Option option, Frame[] parameters) {
		return new FrameBuilder("option")
				.add("name", option.name$())
				.add("future", option.core$().owner().name())
				.add("parameter", parameters)
				.add("optionParameter", framesOf(option.parameterList())).toFrame();
	}

	private Frame[] framesOf(List<Parameter> parameters) {
		return IntStream.range(0, parameters.size()).mapToObj(i -> frameOf(parameters.get(i), i)).toArray(Frame[]::new);
	}

	private Frame frameOf(Parameter param, int index) {
		String innerPackage = param.isObject() && param.asObject().isComponent() ? String.join(".", packageName(), "schemas.") : "";
		final FrameBuilder builder = new FrameBuilder("parameter", param.asType().type())
				.add("index", index)
				.add("name", param.name$());
		if (param.isWord()) builder.add("type", "java.lang.String");
		else builder.add("type", innerPackage + param.asType().type());
		if (param.i$(Data.List.class)) builder.add("list");
		return builder.toFrame();
	}

	private Template template() {
		return Formatters.customize(new AgendaServiceTemplate());
	}
}
