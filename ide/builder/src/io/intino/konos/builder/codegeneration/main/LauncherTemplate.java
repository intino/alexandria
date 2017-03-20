package io.intino.konos.builder.codegeneration.main;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class LauncherTemplate extends Template {

	protected LauncherTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new LauncherTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "boxConfiguration"))).add(literal("package ")).add(mark("package")).add(literal(";\n\npublic class Launcher {\n\n\tpublic static class Pre {\n\n\t\tpublic static void main(String[] args) throws Exception {\n\t\t\tMain.main(new String[]{\n\t\t\t\t\t")).add(expression().add(mark("tara", "empty")).add(literal("\"graph.store=\","))).add(literal("\n\t\t\t\t\t")).add(mark("service", "fill").multiple(",\n")).add(literal("\n\t\t\t});\n\t\t}\n\t}\n\n\tpublic static class Pro {\n\n\t\tpublic static void main(String[] args) throws Exception {\n\t\t\tMain.main(new String[]{\n\t\t\t\t\t")).add(expression().add(mark("tara", "empty")).add(literal("\"graph.store=\","))).add(literal("\n\t\t\t\t\t")).add(mark("service", "fill").multiple(",\n")).add(literal("\n\t\t\t});\n\t\t}\n\t}\n\n}")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "fill"))).add(literal("\"")).add(mark("name", "firstLowerCase")).add(literal(".port=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".webDirectory=\"")).add(expression().add(mark("custom", "parameter").multiple(""))),
			rule().add((condition("type", "service & jms")), (condition("trigger", "fill"))).add(literal("\"")).add(mark("name", "firstLowerCase")).add(literal(".url=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".user=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".password=\"")).add(expression().add(mark("custom", "parameter").multiple(""))),
			rule().add((condition("type", "service & bus")), (condition("trigger", "fill"))).add(literal("\"")).add(mark("name", "firstLowerCase")).add(literal(".url=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".user=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".password=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".clientID=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".productionPaths=\"")).add(expression().add(mark("custom", "parameter").multiple(""))),
			rule().add((condition("type", "service & channel")), (condition("trigger", "fill"))).add(expression().add(mark("custom", "parameter").multiple(""))),
			rule().add((condition("type", "service & slack")), (condition("trigger", "fill"))).add(literal("\"")).add(mark("name", "firstLowerCase")).add(literal(".token=\"")),
			rule().add((condition("type", "service & activity")), (condition("trigger", "fill"))).add(literal("\"")).add(mark("name", "firstLowerCase")).add(literal(".port=\",\n\"")).add(mark("name", "firstLowerCase")).add(literal(".webDirectory=\"")).add(expression().add(mark("custom", "parameter").multiple(""))),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "fill"))).add(literal("\"")).add(mark("name", "firstLowerCase")).add(literal(".port=\"")).add(expression().add(mark("custom", "parameter").multiple(""))),
			rule().add((condition("type", "custom")), (condition("trigger", "parameter"))).add(literal(",")).add(literal("\n")).add(literal("\"")).add(mark("conf", "validname", "firstLowerCase")).add(literal(".")).add(mark("name", "validname", "firstLowerCase")).add(literal("=\"")),
			rule().add((condition("trigger", "parameter"))),
			rule().add((condition("trigger", "empty")))
		);
		return this;
	}
}