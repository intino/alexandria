package io.intino.konos.builder.codegeneration.services.rest;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class ApiPortalConfigurationTemplate extends Template {

	protected ApiPortalConfigurationTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ApiPortalConfigurationTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "api"))).add(literal("{\n  \"urls\": [\n    ")).add(mark("url").multiple("\n")).add(literal("\n  ],\n  \"title\": \"")).add(mark("title")).add(literal("\",\n  \"subtitle\": \"")).add(mark("subtitle")).add(literal("\",\n  \"background\": \"")).add(mark("background")).add(literal("\",\n  \"color\": \"")).add(mark("color")).add(literal("\",\n  \"selectorBorderColor\": \"\"\n}")),
			rule().add((condition("trigger", "url"))).add(literal("{\n  \"url\": \"./data/")).add(mark("value")).add(literal(".json\",\n  \"name\": \"")).add(mark("value")).add(literal("\"\n}"))
		);
		return this;
	}
}