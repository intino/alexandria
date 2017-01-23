package io.intino.pandora.builder.actions;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MavenMetadataTemplate extends Template {

	protected MavenMetadataTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MavenMetadataTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "metadata"))).add(literal("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<metadata>\n  <groupId>")).add(mark("group", "lowercase")).add(literal("</groupId>\n  <artifactId>")).add(mark("artifact", "lowercase")).add(literal("</artifactId>\n  <version>")).add(mark("version")).add(literal("</version>\n  <versioning>\n    <latest>")).add(mark("version")).add(literal("</latest>\n    <release>")).add(mark("version")).add(literal("</release>\n    <versions>\n      <version>")).add(mark("version")).add(literal("</version>\n      ")).add(mark("oldversion").multiple("\n")).add(literal("\n    </versions>\n    <lastUpdated>")).add(mark("time")).add(literal("</lastUpdated>\n  </versioning>\n</metadata>")),
			rule().add((condition("trigger", "oldversion"))).add(literal("<version>")).add(mark("value")).add(literal("</version>"))
		);
		return this;
	}
}