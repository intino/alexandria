package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DesktopTemplate extends Template {

	protected DesktopTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DesktopTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "desktop & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDesktop;\nimport io.intino.konos.alexandria.activity.model.Desktop;\nimport io.intino.konos.alexandria.activity.utils.Base64;\nimport io.intino.konos.alexandria.activity.utils.IOUtils;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\nimport java.net.MalformedURLException;\nimport java.net.URL;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal(" extends AlexandriaDesktop<")).add(mark("name", "firstUpperCase")).add(literal("Notifier> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildDesktop(box));\n\t}\n\n\tprivate static Desktop buildDesktop(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tDesktop desktop = new Desktop();\n\t\t")).add(expression().add(literal("desktop.title(\"")).add(mark("title")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.subtitle(\"")).add(mark("subtitle")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.authServiceUrl(box.configuration().")).add(mark("activity", "firstLowerCase")).add(literal("Configuration().authServiceUrl());"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.logo(toBase64(Abstract")).add(mark("name", "firstUpperCase")).add(literal(".class.getResourceAsStream(\"")).add(mark("logo")).add(literal("\")));"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.favicon(toBase64(Abstract")).add(mark("name", "firstUpperCase")).add(literal(".class.getResourceAsStream(\"")).add(mark("favicon")).add(literal("\")));"))).add(literal("\n\t\tdesktop.layoutDisplayProvider(() -> ")).add(mark("package", "validPackage")).add(literal(".displays.")).add(mark("name", "firstUpperCase")).add(literal(".layout(box));\n\t\treturn desktop;\n\t}\n\n\tprivate static String toBase64(java.io.InputStream resource) {\n\t\ttry {\n\t\t\tif (resource == null) return \"\";\n\t\t\treturn \"data:image/png;base64,\" + Base64.encode(toByteArray(resource));\n\t\t} catch (java.io.IOException e) {\n\t\t\treturn \"\";\n\t\t}\n\t}\n\n\tprivate static byte[] toByteArray(java.io.InputStream input) throws java.io.IOException {\n\t\tjava.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();\n\t\tIOUtils.copy(input, output);\n\t\treturn output.toByteArray();\n\t}\n}")),
			rule().add((condition("type", "desktop"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaLayout;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n    public ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "FisrtUpperCase")).add(literal("Box box) {\n        super(box);\n    }\n\n\tpublic static AlexandriaLayout layout(")).add(mark("box", "FisrtUpperCase")).add(literal("Box box) {\n\t\treturn new ")).add(mark("layout", "FirstUpperCase")).add(literal("(box);\n\t}\n}"))
		);
		return this;
	}
}