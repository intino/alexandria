package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Exception;
import io.intino.konos.dsl.*;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.itrules.formatters.StringFormatters.camelCase;
import static io.intino.itrules.formatters.StringFormatters.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public abstract class ActionRenderer extends Renderer {
	private final String[] types;

	public enum ContextType {Default, Spark}

	public ActionRenderer(CompilationContext context, String... types) {
		super(context);
		this.types = types;
	}

	protected ContextType contextType() {
		return ContextType.Default;
	}

	protected boolean alreadyRendered(File destiny, String action) {
		return alreadyRendered(destiny, action, Target.Server);
	}

	protected boolean alreadyRendered(File destiny, String action, Target target) {
		return javaFile(destinationPackage(destiny), firstUpperWithCamel(action) + suffix(target)).exists();
	}

	protected File destinationPackage(File destiny) {
		return new File(destiny, "actions");
	}

	protected void execute(String name, String serviceName, Response response, Map<String, ? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
		File destiny = destination();
		if (!alreadyRendered(destiny, name)) {
			createNewClass(name, serviceName, response, parameters, exceptions, schemas);
		} else {
			File newDestination = javaFile(destinationPackage(destiny), firstUpperWithCamel(name) + suffix(Target.Server));
			new ActionUpdater(context, newDestination, packageName(), parameters, exceptions, response).update();
		}
	}

	protected void execute(String name, String serviceName, Response response, List<? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
		File destiny = destination();
		if (!alreadyRendered(destiny, name))
			createNewClass(name, serviceName, response, toMap(parameters), exceptions, schemas);
		else {
			File newDestination = javaFile(destinationPackage(destiny), firstUpperWithCamel(name) + suffix(Target.Server));
			new ActionUpdater(context, newDestination, packageName(), toMap(parameters), exceptions, response).update();
		}
	}

	private Map<String, ? extends Parameter> toMap(List<? extends Parameter> parameters) {
		return parameters.stream().collect(Collectors.toMap(Layer::name$, p -> p));
	}

	protected String suffix(Target target) {
		return "Action";
	}

	private void createNewClass(String name, String serviceName, Response response, Map<String, ? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
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
		File packageFolder = destinationPackage(destination());
		context.compiledFiles().add(new OutputItem(packageFolder.getAbsolutePath(), javaFile(packageFolder, firstUpperWithCamel(name) + suffix(Target.Server)).getAbsolutePath()));
		Commons.writeFrame(packageFolder, firstUpperWithCamel(name) + suffix(Target.Server), new ActionTemplate().render(builder.toFrame(), Formatters.all));
	}

	private static String firstUpperWithCamel(String name) {
		return (String) firstUpperCase().format(camelCase().format(name));
	}

	protected FrameBuilder contextPropertyFrame() {
		FrameBuilder result = new FrameBuilder("contextProperty");
		if (contextType() == ContextType.Spark) result.add("spark");
		return result;
	}

	private void setupParameters(Map<String, ? extends Parameter> parameters, FrameBuilder builder) {
		for (Map.Entry<String, ? extends Parameter> parameter : parameters.entrySet()) {
			final FrameBuilder parameterBuilder = new FrameBuilder("parameter", parameter.getValue().asType().getClass().getSimpleName());
			if (parameter.getValue().isList()) parameterBuilder.add("list");
			builder.add("parameter", parameterBuilder.add("name", camelCase().format(parameter.getKey())).add("type", formatType(parameter.getValue().asType())).toFrame());
		}
	}

	private String formatType(Data.Type typeData) {
		return (typeData.i$(Data.Object.class) ? (packageName() + ".schemas.") : "") + typeData.type();
	}

	private File destination() {
		return src(Target.Server);
	}
}
