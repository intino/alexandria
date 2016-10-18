package io.intino.pandora.plugin.codegeneration.action;

import io.intino.pandora.plugin.Exception;
import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.Response;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.object.ObjectData;
import io.intino.pandora.plugin.type.TypeData;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

abstract class ActionRenderer {
	protected final File destiny;
	protected String packageName;

	ActionRenderer(File destiny, String packageName) {
		this.destiny = destiny;
		this.packageName = packageName;
	}

	protected Template template() {
		final Template template = ActionTemplate.create();
		template.add("ValidPackage", Commons::validPackage);
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		return template;
	}

	boolean alreadyRendered(File destiny, String action) {
		return Commons.javaFile(destinyPackage(destiny), action + "Action").exists();
	}

	File destinyPackage(File destiny) {
		return new File(destiny, "actions");
	}

	String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	protected void execute(String name, Response response, List<? extends Parameter> parameters, List<Exception> exceptions, List<Schema> schemas) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", name);
		frame.addSlot("package", packageName);
		setupParameters(parameters, frame);
		frame.addSlot("returnType", Commons.returnType(response));
		if (!exceptions.isEmpty())
			frame.addSlot("throws", exceptions.stream().map(e -> e.code().name()).toArray(String[]::new));
		if (!schemas.isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		if (!alreadyRendered(destiny, name))
			Commons.writeFrame(destinyPackage(destiny), firstUpperCase(name) + "Action", template().format(frame));
	}


	private void setupParameters(List<? extends Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters)
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", parameter.name()).addSlot("type", formatType(parameter.asType())));
	}

	protected String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}
