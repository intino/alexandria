package io.intino.konos.builder.codegeneration.services.ui.display.editor;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class EditorTemplate extends Template {

	protected EditorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new EditorTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "editor"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays;\n\nimport cottons.utils.StreamHelper;\nimport io.intino.konos.alexandria.ui.Resource;\nimport io.intino.konos.alexandria.ui.displays.AlexandriaEditor;\nimport io.intino.konos.alexandria.ui.model.Element;\nimport io.intino.konos.alexandria.ui.services.push.UISession;\nimport io.intino.konos.alexandria.ui.services.EditorService;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".dialogs.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.*;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t@Override\n\tpublic void prepare(")).add(mark("display", "firstUpperCase")).add(literal(" dialog, io.intino.konos.alexandria.schema.Resource document, EditorService.Permission permission) {\n\t}\n\n\t@Override\n\tpublic void save() {\n\t}\n}"))
		);
		return this;
	}
}