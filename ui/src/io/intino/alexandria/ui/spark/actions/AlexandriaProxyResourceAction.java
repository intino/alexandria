package io.intino.alexandria.ui.spark.actions;

import java.net.URL;


public abstract class AlexandriaProxyResourceAction extends AlexandriaResourceAction {

	public AlexandriaProxyResourceAction() {
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