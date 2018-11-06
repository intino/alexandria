package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class AbstractPanelSkeletonTemplate extends Template {

	protected AbstractPanelSkeletonTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractPanelSkeletonTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "desktop"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.alexandria.Base64;\nimport io.intino.alexandria.ui.displays.AlexandriaDesktop;\nimport io.intino.alexandria.ui.displays.AlexandriaElementDisplay;\nimport io.intino.alexandria.ui.model.Element;\nimport io.intino.alexandria.ui.model.panel.Desktop;\nimport io.intino.alexandria.ui.model.View;\nimport io.intino.alexandria.ui.utils.IOUtils;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\nimport java.net.MalformedURLException;\nimport java.net.URL;\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal(" extends AlexandriaDesktop<")).add(mark("name", "firstUpperCase")).add(literal("Notifier> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildDesktop(box));\n\t}\n\n\tprivate static Desktop buildDesktop(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tDesktop desktop = new Desktop();\n\t\tdesktop.elementDisplayBuilder(new Desktop.ElementDisplayBuilder() {\n\t\t\t@Override\n\t\t\tpublic AlexandriaElementDisplay displayFor(Element element, Object o) {\n\t\t\t\treturn get(element, o);\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Object o) {\n\t\t\t\treturn get(element, o).getClass();\n\t\t\t}\n\n\t\t\tprivate AlexandriaElementDisplay get(Element element, Object o) {\n\t\t\t\treturn Displays.displayFor(box, element);\n\t\t\t}\n\t\t});\n\t\t")).add(expression().add(literal("desktop.title(\"")).add(mark("title")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.subtitle(\"")).add(mark("subtitle")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.authServiceUrl(")).add(mark("authentication")).add(literal(");"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.logo(toBase64(Abstract")).add(mark("name", "firstUpperCase")).add(literal(".class.getResourceAsStream(\"")).add(mark("logo")).add(literal("\")));"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.favicon(toBase64(Abstract")).add(mark("name", "firstUpperCase")).add(literal(".class.getResourceAsStream(\"")).add(mark("favicon")).add(literal("\")));"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.layout(Desktop.Layout.")).add(mark("layout")).add(literal(");"))).add(literal("\n\t\t")).add(expression().add(mark("toolbar", "empty")).add(literal("desktop.toolbar(buildToolbar(box));"))).add(literal("\n\t\tbuildViews(box).forEach(v -> desktop.add(v));\n\t\treturn desktop;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate static List<View> buildViews(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<View> result = new ArrayList<>();\n\t\t")).add(mark("view", "add").multiple("\n")).add(literal("\n\t\treturn result;\n\t}\n\n\tprivate static String toBase64(java.io.InputStream resource) {\n\t\ttry {\n\t\t\tif (resource == null) return \"\";\n\t\t\treturn \"data:image/png;base64,\" + Base64.encode(toByteArray(resource));\n\t\t} catch (java.io.IOException e) {\n\t\t\treturn \"\";\n\t\t}\n\t}\n\n\tprivate static java.net.URL url(String url) {\n\t\ttry {\n\t\treturn new java.net.URL(url);\n\t\t} catch (java.net.MalformedURLException e) {\n\t\t\treturn null;\n\t\t}\n\t}\n\n\tprivate static byte[] toByteArray(java.io.InputStream input) throws java.io.IOException {\n\t\tjava.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();\n\t\tIOUtils.copy(input, output);\n\t\treturn output.toByteArray();\n\t}\n}")),
			rule().add((condition("type", "custom")), (condition("trigger", "authentication"))).add(literal("url(box.configuration().get(\"")).add(mark("value", "customParameter")).add(literal("\"))")),
			rule().add((condition("trigger", "authentication"))).add(literal("url(\"")).add(mark("value")).add(literal("\")")),
				rule().add((condition("type", "panel"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.alexandria.ui.displays.AlexandriaDisplay;\nimport io.intino.alexandria.ui.displays.AlexandriaPanel;\nimport io.intino.alexandria.ui.displays.CatalogInstantBlock;\nimport io.intino.alexandria.ui.model.View;\nimport io.intino.alexandria.ui.model.Panel;\nimport io.intino.alexandria.ui.model.Toolbar;\nimport io.intino.alexandria.ui.model.View;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\nimport java.util.function.Consumer;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal(" extends AlexandriaPanel<")).add(mark("name", "firstUpperCase")).add(literal("Notifier> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildPanel(box));\n\t}\n\n\tprivate static Panel buildPanel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tPanel panel = new Panel();\n\t\t")).add(expression().add(literal("panel.name(\"")).add(mark("name")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("panel.label(\"")).add(mark("label")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(mark("toolbar", "empty")).add(literal("panel.toolbar(buildToolbar(box));"))).add(literal("\n\t\tbuildViews(box).forEach(v -> panel.add(v));\n\t\treturn panel;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate static List<View> buildViews(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<View> result = new ArrayList<>();\n\t\t")).add(mark("view", "add").multiple("\n")).add(literal("\n\t\treturn result;\n\t}\n\n}")),
			rule().add((condition("type", "toolbar")), (condition("trigger", "empty"))),
			rule().add((condition("trigger", "add"))).add(literal("result.add(")).add(mark("value")).add(literal(");")),
			rule().add((condition("type", "toolbar"))).add(literal("private static Toolbar buildToolbar(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\tToolbar result = new Toolbar();\n\tresult.canSearch(")).add(mark("canSearch")).add(literal(");\n\t")).add(mark("operation", "add").multiple("\n")).add(literal("\n\treturn result;\n}"))
		);
		return this;
	}
}