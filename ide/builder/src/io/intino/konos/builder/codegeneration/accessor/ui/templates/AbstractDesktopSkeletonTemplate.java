package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractDesktopSkeletonTemplate extends Template {

	protected AbstractDesktopSkeletonTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractDesktopSkeletonTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "desktop"))).add(literal("import React from \"react\";\nimport * as Ui from \"../../../lib/alexandria-ui-elements/UiElements\";\nimport AlexandriaDesktop from \"../../../lib/alexandria-ui-elements/src/displays/Desktop\";")).add(expression().add(literal("\n")).add(literal("import ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier from \"../notifiers/")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier\";"))).add(expression().add(literal("\n")).add(literal("import ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester from \"../requesters/")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester\";"))).add(literal("\n\nexport default class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal(" extends AlexandriaDesktop {\n\n\tconstructor(props) {\n\t\tsuper(props);")).add(expression().add(literal("\n")).add(literal("\t\tthis.notifier = new ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier(this);"))).add(expression().add(literal("\n")).add(literal("\t\tthis.requester = new ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester(this);"))).add(literal("\n\t};\n\n\trender() {\n\t\treturn (\n\t\t\t<React.Fragment>\n\t\t\t\t<Ui.AppBar id=\"appBar\">\n\t\t\t\t\t")).add(mark("component").multiple("\n")).add(literal("\n\t\t\t\t</Ui.AppBar>\n\t\t\t\t<Ui.Tabs id=\"tabBar\">\n\t\t\t\t\t")).add(mark("tabs").multiple("\n")).add(literal("\n\t\t\t\t</Ui.Tabs>\n\t\t\t</React.Fragment>\n\t\t);\n\t}\n}"))
		);
		return this;
	}
}