package io.intino.konos.alexandria.ui.spark.actions;

import java.net.URL;


public abstract class AlexandriaProxyResourceAction extends AlexandriaResourceAction {

	public AlexandriaProxyResourceAction(String uiServiceName) {
		super(uiServiceName);
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