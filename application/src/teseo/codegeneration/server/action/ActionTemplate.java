package teseo.codegeneration.server.action;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ActionTemplate extends Template {

	protected ActionTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ActionTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "action"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".scheduling;\n\nimport teseo.framework.scheduling.*;\nimport ")).add(mark("package", "validname")).add(literal(".*;\nimport tara.magritte.Graph;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Action {\n\n\tpublic void execute(Graph graph) {\n\n\t}\n}"))
		);
		return this;
	}
}