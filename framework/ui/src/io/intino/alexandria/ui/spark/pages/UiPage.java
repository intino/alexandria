package io.intino.alexandria.ui.spark.pages;

import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.services.push.Browser;
import io.intino.alexandria.ui.services.push.UIClient;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UiPage extends Page {

	public Soul prepareSoul(UIClient client) { return null; }
	public abstract String execute();

}