package io.intino.pandora.plugin.codegeneration.server.ui.display;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayTemplate extends Template {

	protected DisplayTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".displays.notifiers;\n\nimport io.intino.pandora.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport ")).add(mark("package", "validname")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n")).add(mark("schemaImport")).add(literal("\nimport com.monentia.ebar.pandora.displays.notifiers.ApplicationDisplayNotifier;\nimport io.intino.pandora.server.activity.displays.Display;\nimport io.intino.pandora.server.activity.services.push.User;\n\npublic class ApplicationDisplay extends Display<ApplicationDisplayNotifier> {\n    private EbarBox box;\n\n    public ApplicationDisplay(EbarBox box) {\n        super();\n        this.box = box;\n    }\n\n    public void user(User user) {\n        notifier.userRefresh(schemaUser(user));\n        personifyStationCatalogDisplay(user);\n    }\n\n    private com.monentia.ebar.pandora.schemas.User schemaUser(User user) {\n        return new com.monentia.ebar.pandora.schemas.User()\n                .username(user.username())\n                .fullName(user.fullName())\n                .email(user.email())\n                .photo(user.photo().toString())\n                .language(user.language());\n    }\n\n    public void logout() {\n        notifier.userLoggedOut();\n    }\n\n    private void personifyStationCatalogDisplay(User user) {\n        StationCatalogDisplay display = new StationCatalogDisplay(box);\n        addAndPersonify(display);\n        display.refresh();\n    }\n\n}\n"))
		);
		return this;
	}
}