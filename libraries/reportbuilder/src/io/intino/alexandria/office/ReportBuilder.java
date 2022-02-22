package io.intino.alexandria.office;


import java.io.File;
import java.io.InputStream;

public abstract class ReportBuilder {
	protected String title;

	public <T extends ReportBuilder> T title(String title) {
		this.title = title;
		return (T) this;
	}

	public abstract InputStream build(File source);
}
