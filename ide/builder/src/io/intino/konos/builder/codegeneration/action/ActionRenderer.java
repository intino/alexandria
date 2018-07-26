package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.object.ObjectData;
import io.intino.konos.model.graph.type.TypeData;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;

abstract class ActionRenderer {
	protected final Project project;
	protected final File destiny;
	protected String packageName;
	protected final String boxName;

	ActionRenderer(Project project, File destiny, String packageName, String boxName) {
		this.project = project;
		this.destiny = destiny;
		this.packageName = packageName;
		this.boxName = boxName;
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
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", name);
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		setupParameters(parameters, frame);
		frame.addSlot("returnType", Commons.returnType(response, packageName));
		if (!exceptions.isEmpty())
			frame.addSlot("throws", exceptions.stream().map(e -> e.code().name()).toArray(String[]::new));
		if (!schemas.isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		Commons.writeFrame(destinyPackage(destiny), firstUpperCase(snakeCaseToCamelCase(name)) + "Action", template().format(frame));
	}

	private void setupParameters(List<? extends Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters) {
			final Frame parameterFrame = new Frame().addTypes("parameter", parameter.asType().getClass().getSimpleName());
			if (parameter.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame.addSlot("name", snakeCaseToCamelCase().format(parameter.name$())).addSlot("type", formatType(parameter.asType())));
		}
	}

	private String formatType(TypeData typeData) {
		return (typeData.i$(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	protected String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	protected Template template() {
		return Formatters.customize(ActionTemplate.create());
	}
}
