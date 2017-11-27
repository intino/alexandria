package io.intino.konos.builder.codegeneration.services.activity.resource;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AssetResourceLoaderTemplate extends Template {

	protected AssetResourceLoaderTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AssetResourceLoaderTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "resourceloader"))).add(literal("package ")).add(mark("package")).add(literal(";\n\npublic class AssetResourceLoader {\n\n\tprivate final ")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box box;\n\n\tpublic AssetResourceLoader(")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic java.net.URL load(String name) {\n\t\treturn null;\n\t}\n\n}\n"))
		);
		return this;
	}
}