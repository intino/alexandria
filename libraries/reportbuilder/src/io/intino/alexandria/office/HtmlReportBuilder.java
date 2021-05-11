package io.intino.alexandria.office;


import io.intino.alexandria.office.builders.HtmlReportGenerator;
import io.intino.itrules.Template;

import java.io.File;
import java.io.InputStream;

public class HtmlReportBuilder extends ReportBuilder {
	private Template template;

	public static HtmlReportBuilder create() {
		return new HtmlReportBuilder();
	}

	public HtmlReportBuilder template(Template template) {
		this.template = template;
		return this;
	}

	public InputStream build(File source) {
		return new HtmlReportGenerator(template).generate(title, source);
	}
}
