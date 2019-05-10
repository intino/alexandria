package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.object.ObjectData;
import io.intino.konos.model.graph.type.TypeData;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;

abstract class ActionRenderer {
	protected final Project project;
	protected final File destiny;
	protected final String boxName;
	private final String type;
	protected String packageName;

	ActionRenderer(Project project, File destiny, String packageName, String boxName, String type) {
		this.project = project;
		this.destiny = destiny;
		this.packageName = packageName;
		this.boxName = boxName;
		this.type = type;
	}

	boolean alreadyRendered(File destiny, String action) {
		return Commons.javaFile(destinyPackage(destiny), firstUpperCase(snakeCaseToCamelCase(action)) + "Action").exists();
	}

	File destinyPackage(File destiny) {
		return new File(destiny, "actions");
	}

	protected void execute(String name, Response response, List<? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
		if (!alreadyRendered(destiny, name)) {
			createNewClass(name, response, parameters, exceptions, schemas);
		} else {
			File destiny = Commons.javaFile(destinyPackage(this.destiny), firstUpperCase(snakeCaseToCamelCase(name)) + "Action");
			new ActionUpdater(project, destiny, packageName, parameters, exceptions, response).update();
		}
	}

	private void createNewClass(String name, Response response, List<? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
		FrameBuilder builder = new FrameBuilder("action", this.type)
				.add("name", name)
				.add("package", packageName)
				.add("box", boxName)
				.add("returnType", Commons.returnType(response, packageName));
		setupParameters(parameters, builder);

		if (!exceptions.isEmpty())
			builder.add("throws", exceptions.stream().map(e -> e.code().name()).toArray(String[]::new));
		if (!schemas.isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName).toFrame());
		Commons.writeFrame(destinyPackage(destiny), firstUpperCase(snakeCaseToCamelCase(name)) + "Action", template().render(builder.toFrame()));
	}

	private void setupParameters(List<? extends Parameter> parameters, FrameBuilder builder) {
		for (Parameter parameter : parameters) {
			final FrameBuilder parameterBuilder = new FrameBuilder("parameter", parameter.asType().getClass().getSimpleName());
			if (parameter.isList()) parameterBuilder.add("list");
			builder.add("parameter", parameterBuilder.add("name", snakeCaseToCamelCase().format(parameter.name$())).add("type", formatType(parameter.asType())).toFrame());
		}
	}

	private String formatType(TypeData typeData) {
		return (typeData.i$(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	protected String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	protected Template template() {
		return Formatters.customize(new ActionTemplate());
	}
}
