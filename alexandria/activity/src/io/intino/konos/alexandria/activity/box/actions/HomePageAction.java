package io.intino.konos.alexandria.activity.box.actions;

import io.intino.konos.alexandria.activity.box.ActivityBox;
import io.intino.konos.alexandria.activity.box.spark.actions.AlexandriaPageAction;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaDesktopDisplay;

public class HomePageAction extends AlexandriaPageAction {

	// TODO Octavio -> ActivityBox en vez de AlexandriaBox
	public ActivityBox box;


	public HomePageAction() { super("alexandria"); }

	public String execute() {
		return super.template("homePage");
	}

	public io.intino.konos.alexandria.activity.box.displays.Soul prepareSoul(io.intino.konos.alexandria.activity.box.services.push.ActivityClient client) {
	    return new io.intino.konos.alexandria.activity.box.displays.Soul(session) {
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