package io.intino.konos.builder.codegeneration.exception;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static java.util.stream.Collectors.toList;

public class ExceptionRenderer extends Renderer {
	private static final String EXCEPTIONS = "exceptions";
	private final List<io.intino.konos.model.graph.Exception> exceptions;

	public ExceptionRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.exceptions = graph.exceptionList();
	}

	public void clean() {
		File folder = destinyPackage(gen());
		if (!folder.exists()) return;
		List<String> filenames = exceptions.stream().map(e -> javaFile(folder, e.name$()).getAbsolutePath()).collect(toList());
		Arrays.stream(Objects.requireNonNull(folder.listFiles((file, name) -> !filenames.contains(name)))).forEach(File::delete);
	}

	public void render() {
		exceptions.forEach(this::processException);
	}

	private void processException(io.intino.konos.model.graph.Exception exception) {
		writeFrame(destinyPackage(gen()), exception.name$(), template().render(
				new FrameBuilder("exception")
						.add("name", exception.name$())
						.add("code", exception.code())
						.add("package", packageName()).toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(exception), javaFile(destinyPackage(gen()), exception.name$()).getAbsolutePath()));
	}

	private File destinyPackage(File destiny) {
		return new File(destiny, EXCEPTIONS);
	}

	private Template template() {
		return new ExceptionTemplate();
	}
}
