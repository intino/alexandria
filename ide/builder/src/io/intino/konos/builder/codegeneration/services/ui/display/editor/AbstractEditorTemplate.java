package io.intino.konos.builder.codegeneration.services.ui.display.editor;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class AbstractEditorTemplate extends Template {

	protected AbstractEditorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractEditorTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "editor & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".dialogs.*;\nimport io.intino.alexandria.ui.displays.AlexandriaEditor;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" extends io.intino.alexandria.ui.displays.AlexandriaEditor<")).add(mark("display", "FirstUpperCase")).add(literal("> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n}"))
		);
		return this;
	}
}