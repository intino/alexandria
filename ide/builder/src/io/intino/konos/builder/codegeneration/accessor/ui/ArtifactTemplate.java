package io.intino.konos.builder.codegeneration.accessor.ui;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class ArtifactTemplate extends Template {

	protected ArtifactTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ArtifactTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "artifact & legio"))).add(literal("dsl Legio\n\nArtifact(groupId = \"")).add(mark("groupID")).add(literal("\", version = \"1.0.0\") ")).add(mark("artifactID")).add(literal("UI\n\tWebImports\n\t\tWebArtifact(\"io.intino.alexandria\", \"ui-elements\", \"LATEST\") alexandria-ui-elements\n\n\t\tResolution(\"jquery\", \"3.1.0\")\n")).add(mark("repository")),
			rule().add((condition("type", "repository"))).add(literal("Repository(\"")).add(mark("id")).add(literal("\")\n\t")).add(mark("url").multiple("\n")),
			rule().add((condition("trigger", "url"))).add(literal("Release(\"")).add(mark("value")).add(literal("\")"))
		);
		return this;
	}
}