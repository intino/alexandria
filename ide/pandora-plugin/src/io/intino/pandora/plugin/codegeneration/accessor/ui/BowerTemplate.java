package io.intino.pandora.plugin.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class BowerTemplate extends Template {

	protected BowerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BowerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "configuration"))).add(literal("{\n  \"name\": \"")).add(mark("application")).add(literal("\",\n  \"version\": \"1.0.0\",\n  \"dependencies\": {\n\t\"polymer\": \"Polymer/polymer#^1.4.0\",\n\t\"jquery\": \"3.1.0\",\n\t\"moment\": \"2.13.0\",\n\t\"numeral\": \"numeraljs#^1.5.3\",\n\t\"cotton-carrier\": \"https://bitbucket.org/cottons/cotton-carrier.git\",\n\t\"cotton-push\": \"https://bitbucket.org/cottons/cotton-push.git\",\n\t\"cotton-translator\": \"https://bitbucket.org/cottons/cotton-translator.git\",\n\t\"cotton-zombie\": \"https://bitbucket.org/cottons/cotton-zombie.git\"\n  },\n  \"resolutions\": {\n\t\"jquery\": \"3.1.0\"\n  }\n}"))
		);
		return this;
	}
}