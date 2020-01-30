package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;

public abstract class ActionRenderer extends Renderer {
	private final String[] types;

	public enum ContextType { Default, Spark }

	public ActionRenderer(CompilationContext compilationContext, String... types) {
		super(compilationContext, Target.Owner);
		this.types = types;
	}

	protected ContextType contextType() {
		return ContextType.Default;
	}

	protected boolean alreadyRendered(File destiny, String action) {
		return Commons.javaFile(destinyPackage(destiny), firstUpperCase(snakeCaseToCamelCase(action)) + suffix()).exists();
	}

	protected File destinyPackage(File destiny) {
		return new File(destiny, "actions");
	}

	protected void execute(String name, String serviceName, Response response, List<? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
		File destiny = destiny();
		if (!alreadyRendered(destiny, name)) {
			createNewClass(name, serviceName, response, parameters, exceptions, schemas);
		} else {
			File newDestiny = Commons.javaFile(destinyPackage(destiny), firstUpperCase(snakeCaseToCamelCase(name)) + suffix());
			new ActionUpdater(compilationContext, newDestiny, packageName(), parameters, exceptions, response).update();
		}
	}

	protected String suffix() {
		return "Action";
	}

	private void createNewClass(String name, String serviceName, Response response, List<? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
		String packageName = packageName();
		FrameBuilder builder = new FrameBuilder("action")
				.add("name", name)
				.add("service", serviceName)
				.add("package", packageName)
				.add("box", boxName())
				.add("returnType", Commons.returnType(response, packageName));
		Arrays.stream(types).forEach(builder::add);

		builder.add("contextProperty", contextPropertyFrame());
		setupParameters(parameters, builder);

		if (!exceptions.isEmpty())
			builder.add("throws", exceptions.stream().map(e -> e.code().name()).toArray(String[]::new));
		if (!schemas.isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName).toFrame());
		Commons.writeFrame(destinyPackage(destiny()), firstUpperCase(snakeCaseToCamelCase(name)) + suffix(), template().render(builder.toFrame()));
	}

	protected FrameBuilder contextPropertyFrame() {
		FrameBuilder result = new FrameBuilder("contextProperty");
		if (contextType() == ContextType.Spark) result.add("spark");
		return result;
	}

	private void setupParameters(List<? extends Parameter> parameters, FrameBuilder builder) {
		for (Parameter parameter : parameters) {
			final FrameBuilder parameterBuilder = new FrameBuilder("parameter", parameter.asType().getClass().getSimpleName());
			if (parameter.isList()) parameterBuilder.add("list");
			builder.add("parameter", parameterBuilder.add("name", snakeCaseToCamelCase().format(parameter.name$())).add("type", formatType(parameter.asType())).toFrame());
		}
	}

	private String formatType(Data.Type typeData) {
		return (typeData.i$(Data.Object.class) ? (packageName() + ".schemas.") : "") + typeData.type();
	}

	protected String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	protected Template template() {
		return Formatters.customize(new ActionTemplate());
	}

	private File destiny() {
		return src();
	}
}
