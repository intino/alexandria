package io.intino.konos.builder.codegeneration.services.ui.displays.renderers;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractDisplaySkeletonTemplate extends Template {

	protected AbstractDisplaySkeletonTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractDisplaySkeletonTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays")).add(expression().add(literal(".")).add(mark("packageType")).add(literal("s"))).add(literal(";\n\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.ui.displays.components.*;\nimport ")).add(mark("package")).add(literal(".ui.*;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\nimport io.intino.alexandria.ui.displays.Alexandria")).add(mark("type", "firstUpperCase")).add(literal(";\n\npublic class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal(" extends Alexandria")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("name", "firstUpperCase")).add(literal("Notifier> {\n\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\t")).add(mark("component", "declaration").multiple("\n")).add(literal("\n\n    public ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n        super();\n        this.box = box;\n    }\n\n    @Override\n\tprotected void render() {\n\t\t")).add(mark("component").multiple("\n")).add(literal("\n\t}\n}"))
		);
		return this;
	}
}