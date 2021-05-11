package io.intino.alexandria.office;


import io.intino.alexandria.office.builders.ExcelReportGenerator;
import io.intino.alexandria.office.builders.HtmlReportGenerator;
import io.intino.itrules.Template;

import java.io.File;
import java.io.InputStream;

public class ExcelReportBuilder extends ReportBuilder {
	private File template;

	public static ExcelReportBuilder create() {
		return new ExcelReportBuilder();
	}

	public ExcelReportBuilder template(File template) {
		this.template = template;
		return this;
	}

	public InputStream build(File source) {
		return new ExcelReportGenerator(template).generate(title, source);
	}
}
