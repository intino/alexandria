package io.intino.pandora.plugin.codegeneration.action;

import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.object.ObjectData;
import io.intino.pandora.plugin.type.TypeData;
import org.siani.itrules.Template;

import java.io.File;

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
		return (typeData.is(ObjectData.class) ? (packageName + ".formats.") : "") + typeData.type();
	}
}
