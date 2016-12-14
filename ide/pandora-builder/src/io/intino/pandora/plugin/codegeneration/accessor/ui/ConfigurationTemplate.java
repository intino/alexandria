package io.intino.pandora.plugin.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ConfigurationTemplate extends Template {

	protected ConfigurationTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ConfigurationTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "configuration & legio"))).add(literal("dsl Legio\n\nProject(groupId = \"")).add(mark("groupID")).add(literal("\", version = \"1.0.0\") ")).add(mark("artifactID")).add(literal("Activity\n\tRepositories\n\t\t")).add(mark("repository")).add(literal("\n\tWebDependencies\n\t\tWebComponent(\"Polymer/polymer\", \"^1.4.0\") polymer\n\t\tWebComponent(version = \"3.1.0\") jquery\n\t\tWebComponent(version = \"2.13.0\") moment\n\t\tWebComponent(\"numeraljs\", \"^1.5.3\") numeral\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-carrier.git\", \"latest\") cotton-carrier\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-push.git\", \"latest\") cotton-push\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-translator.git\", \"latest\") cotton-translator\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-zombie.git\", \"latest\") cotton-zombie")),
			rule().add((condition("type", "repository & distribution")), (condition("trigger", "distribution"))).add(mark("type")).add(literal("(\"")).add(mark("url")).add(literal("\", \"")).add(mark("id")).add(literal("\")")),
			rule().add((condition("type", "repository")), not(condition("type", "distribution"))).add(mark("type")).add(literal("(\"")).add(mark("url")).add(literal("\", \"")).add(mark("id")).add(literal("\")"))
		);
		return this;
	}
}