package io.intino.konos.alexandria.activity.actions;

import io.intino.konos.alexandria.activity.ActivityBox;
import io.intino.konos.alexandria.activity.displays.Soul;
import io.intino.konos.alexandria.activity.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.spark.actions.AlexandriaPageAction;
import io.intino.konos.alexandria.activity.displays.AlexandriaDesktopDisplay;

public class HomePageAction extends AlexandriaPageAction {

	// TODO Octavio -> ActivityBox en vez de AlexandriaBox
	public ActivityBox box;


	public HomePageAction() { super("alexandria"); }

	public String execute() {
		return super.template("homePage");
	}

	public Soul prepareSoul(ActivityClient client) {
	    return new Soul(session) {
			@Override
			public void personify() {
				AlexandriaDesktopDisplay display = new AlexandriaDesktopDisplay(box);
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
	protected String subtitle() {
		return "";
	}

	@Override
	protected java.net.URL logo() {
		return null;
	}

	@Override
	protected java.net.URL icon() {
		return null;
	}
}