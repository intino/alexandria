package io.intino.konos.builder.codegeneration.services.agenda;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.Data;
import io.intino.konos.dsl.Parameter;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

import static io.intino.konos.builder.codegeneration.Formatters.all;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.codegeneration.services.ui.Target.Server;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.dsl.Service.Agenda.Future;

public class FutureRenderer extends Renderer {

	private final Future future;

	public FutureRenderer(CompilationContext context, Future future) {
		super(context);
		this.future = future;
	}

	@Override
	protected void render() throws KonosException {
		FrameBuilder builder = futureFrame()
				.add("package", packageName())
				.add("box", boxName());
		if (!future.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		final Frame frame = builder.toFrame();
		renderSrc(frame);
		renderAbstract(frame);
		renderSchema(frame);
	}

	private void renderSrc(Frame frame) {
		File agendaPackage = new File(src(Server), "agenda");
		File file = javaFile(agendaPackage, firstUpperCase(future.name$()));
		if (file.exists()) return;
		writeFrame(agendaPackage, firstUpperCase(future.name$()), new FutureTemplate().render(frame, all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(future), javaFile(agendaPackage, firstUpperCase(future.name$())).getAbsolutePath()));
	}

	private void renderAbstract(Frame frame) {
		writeFrame(new File(gen(Server), "agenda"), "Abstract" + firstUpperCase(future.name$()), new AbstractFutureTemplate().render(frame, all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(future), javaFile(new File(gen(Server), "agenda"), "Abstract" + firstUpperCase(future.name$())).getAbsolutePath()));
	}

	private void renderSchema(Frame frame) {
		writeFrame(new File(gen(Server), "agenda"), firstUpperCase(future.name$()) + "Schema", new FutureSchemaTemplate().render(frame, all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(future), javaFile(new File(gen(Server), "agenda"), firstUpperCase(future.name$()) + "Schema").getAbsolutePath()));
	}

	private FrameBuilder futureFrame() {
		return new FrameBuilder("future")
				.add("name", future.name$())
				.add("parameter", framesOf(future.parameterList()))
				.add("option", future.optionList().stream().map(option -> frameOf(option, future)).map(FrameBuilder::toFrame).toArray(Frame[]::new));
	}

	private FrameBuilder frameOf(Future.Option option, Future future) {
		return new FrameBuilder("option")
				.add("name", option.name$())
				.add("future", option.core$().owner().name())
				.add("parameter", framesOf(future.parameterList()))
				.add("optionParameter", framesOf(option.parameterList()));
	}

	private Frame[] framesOf(List<Parameter> parameters) {
		return IntStream.range(0, parameters.size()).mapToObj(i -> frameOf(parameters.get(i), i)).toArray(Frame[]::new);
	}

	private Frame frameOf(Parameter param, int index) {
		final FrameBuilder builder = new FrameBuilder("parameter")
				.add("name", param.name$())
				.add("owner", param.core$().owner().name())
				.add("index", index);
		parameterType(param, builder);
		if (param.i$(Data.List.class)) builder.add("list");
		return builder.toFrame();
	}

	private void parameterType(Parameter param, FrameBuilder builder) {
		String innerPackage = param.isObject() && param.asObject().isComponent() ? String.join(".", packageName(), "schemas.") : "";
		if (param.isWord()) builder.add("type", "java.lang.String");
		else builder.add("type", innerPackage + param.asType().type());
	}
}