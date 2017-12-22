package io.intino.konos.alexandria.activity.actions;

import io.intino.konos.alexandria.activity.ActivityBox;
import io.intino.konos.alexandria.activity.displays.AlexandriaDesktop;
import io.intino.konos.alexandria.activity.displays.Soul;
import io.intino.konos.alexandria.activity.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.spark.actions.AlexandriaPageAction;

public class HomePageAction extends AlexandriaPageAction {

	public ActivityBox box;

	public HomePageAction() { super("alexandria"); }

	public String execute() {
		return super.template("homePage");
	}

	public Soul prepareSoul(ActivityClient client) {
	    return new Soul(session) {
			@Override
			public void personify() {
				AlexandriaDesktop display = new AlexandriaDesktop(box);
				register(display);
				display.personify();
			}
		};
	}

	@Override
	protected String title() {
		return "";
	}

	@Override
	protected java.net.URL favicon() {
		return null;
	}
}