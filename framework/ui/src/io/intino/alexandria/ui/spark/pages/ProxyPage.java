package io.intino.alexandria.ui.spark.pages;

import java.net.URL;


public abstract class ProxyPage extends Page {
	public String personifiedDisplay;

	public ProxyPage() {
		super(null);
	}

	@Override
	protected String title() {
		return null;
	}

	@Override
	protected URL favicon() {
		return null;
	}
}