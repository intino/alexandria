package io.intino.konos.builder.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ArtifactTemplate extends Template {

	protected ArtifactTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ArtifactTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "artifact & legio"))).add(literal("dsl Legio\n\nArtifact(groupId = \"")).add(mark("groupID")).add(literal("\", version = \"1.0.0\") ")).add(mark("artifactID")).add(literal("Activity\n\tWebImports\n\t\tWebComponent(\"Polymer/polymer\", \"^1.4.0\") polymer\n\t\tWebComponent(version = \"3.1.0\") jquery\n\t\tWebComponent(version = \"2.13.0\") moment\n\t\tWebComponent(\"numeraljs\", \"^1.5.3\") numeral\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-carrier.git\", \"latest\") cotton-carrier\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-push.git\", \"latest\") cotton-push\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-translator.git\", \"latest\") cotton-translator\n\t\tWebComponent(\"https://bitbucket.org/cottons/cotton-zombie.git\", \"latest\") cotton-zombie\n\n\t\tWebArtifact(\"io.intino.konos\", \"konos-server-web\", \"LATEST\") konos-server-web\n\n\t\tResolution(\"jquery\", \"3.1.0\")\n")).add(mark("repository")),
			rule().add((condition("type", "repository"))).add(literal("Repository(\"")).add(mark("id")).add(literal("\")\n\t")).add(mark("url").multiple("\n")),
			rule().add((condition("trigger", "url"))).add(literal("Release(\"")).add(mark("value")).add(literal("\")"))
		);
		return this;
	}
}