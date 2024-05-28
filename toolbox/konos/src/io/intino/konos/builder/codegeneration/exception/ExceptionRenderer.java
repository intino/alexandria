package io.intino.konos.builder.codegeneration.exception;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.KonosGraph;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static java.util.stream.Collectors.toList;

public class ExceptionRenderer extends Renderer {
	private static final String EXCEPTIONS = "exceptions";
	private final List<io.intino.konos.dsl.Exception> exceptions;

	public ExceptionRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.exceptions = graph.exceptionList();
	}

	public void clean() {
		File folder = destinyPackage(gen(Target.Server));
		if (!folder.exists()) return;
		List<String> filenames = exceptions.stream().map(e -> javaFile(folder, e.name$()).getAbsolutePath()).collect(toList());
		Arrays.stream(Objects.requireNonNull(folder.listFiles((file, name) -> !filenames.contains(name)))).forEach(File::delete);
	}

	public void render() {
		exceptions.forEach(this::processException);
	}

	private void processException(io.intino.konos.dsl.Exception exception) {
		writeFrame(destinyPackage(gen(Target.Server)), exception.name$(), new ExceptionTemplate().render(
				frame(exception), Formatters.all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(exception), javaFile(destinyPackage(gen(Target.Server)), exception.name$()).getAbsolutePath()));
	}

	private Frame frame(io.intino.konos.dsl.Exception exception) {
		return new FrameBuilder("exception")
				.add("name", exception.name$())
				.add("code", exception.code())
				.add("package", packageName()).toFrame();
	}

	private File destinyPackage(File destiny) {
		return new File(destiny, EXCEPTIONS);
	}

}
