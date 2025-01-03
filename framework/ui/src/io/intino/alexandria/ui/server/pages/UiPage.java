package io.intino.alexandria.ui.server.pages;

import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.services.push.UIClient;

public abstract class UiPage extends Page {

	public Soul prepareSoul(UIClient<?> client) { return null; }
	public abstract String execute();

}