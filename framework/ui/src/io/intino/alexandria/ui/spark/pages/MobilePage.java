package io.intino.alexandria.ui.spark.pages;

import io.intino.alexandria.ui.services.push.Browser;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.pages.Unit;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MobilePage extends UiPage {

	protected static final String AppSeparator = "_##_";

	public List<String> pushConnections(List<Unit> usedUnits, String sessionId, String language, Browser browser) {
		List<String> pushList = usedUnits.stream().filter(unit -> unit != null && !unit.url().isEmpty())
											.map(unit -> unit.name() + AppSeparator + browser.pushUrl(sessionId, clientId, language, unit.url()))
											.collect(Collectors.toList());
		pushList.add("Default" + AppSeparator + browser.pushUrl(sessionId, clientId, language));
		return pushList;
	}

}