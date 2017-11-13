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
			rule().add((condition("type", "desktop & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDesktopHome;\nimport io.intino.konos.alexandria.activity.model.Desktop;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\nimport java.net.MalformedURLException;\nimport java.net.URL;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal("Desktop extends AlexandriaDesktopDisplay<")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("Display(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildDesktop(box));\n\t}\n\n\tprivate static Desktop buildDesktop(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tDesktop desktop = new Desktop();\n\t\t")).add(expression().add(literal("desktop.title(\"")).add(mark("title")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.subtitle(\"")).add(mark("subtitle")).add(literal("\");"))).add(literal("\n\t\tdesktop.authServiceUrl(null);\n\t\t")).add(expression().add(literal("desktop.logo(new URL(\"")).add(mark("logo")).add(literal("\"));"))).add(literal("\n\t\t")).add(expression().add(literal("desktop.favicon(new URL(\"")).add(mark("favicon")).add(literal("\"));"))).add(literal("\n\t\t//desktop.authServiceUrl(new URL(\"http://localhost\"));\n\t\tdesktop.layoutDisplayProvider(() -> ")).add(mark("name", "firstUpperCase")).add(literal("Display.Layout.layoutDisplay(box));\n\t\treturn desktop;\n\t}\n}")),
			rule().add((condition("type", "desktop & src"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaLayoutDisplay;\n\npublic class TestHomeHome extends AbstractTestHomeHome {\n\n    public TestHomeHome(TestKonosBox box) {\n        super(box);\n    }\n\n    public static class Layout {\n        public static AlexandriaLayoutDesktop layoutHome(TestKonosBox box) {\n            return new ")).add(mark("name", "FirstUpperCase")).add(literal("Menu(box);\n        }\n    }\n}"))
		);
		return this;
	}
}