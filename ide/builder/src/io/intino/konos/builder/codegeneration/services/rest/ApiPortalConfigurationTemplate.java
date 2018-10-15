package io.intino.konos.builder.codegeneration.services.rest;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ApiPortalConfigurationTemplate extends Template {

	protected ApiPortalConfigurationTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ApiPortalConfigurationTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "api"))).add(literal("{\n  \"url\": ")).add(mark("url", "single")).add(literal(",\n  ")).add(expression().add(literal("\"urls\": ")).add(literal("[")).add(literal("\n")).add(literal("    ")).add(mark("urls", "multiple").multiple("\n")).add(literal("\n")).add(literal("  ")).add(literal("]")).add(literal(","))).add(literal("\n  \"headerText\": \"API Portal\",\n  \"headerColor\": \"\",\n  \"selectorBorderColor\": \"\"\n}")),
			rule().add((condition("trigger", "single"))).add(literal("\"./data/")).add(mark("value")).add(literal(".json\"")),
			rule().add((condition("trigger", "urls"))).add(literal("{\n  \"url\": \"./data/")).add(mark("value")).add(literal(".json\",\n  \"name\": \"")).add(mark("value")).add(literal("\"\n}"))
		);
		return this;
	}
}