package io.intino.konos.alexandria.framework.box.actions;

import io.intino.konos.alexandria.foundation.activity.spark.actions.PageAction;
import io.intino.konos.alexandria.framework.box.AlexandriaFrameworkBox;
import io.intino.konos.alexandria.framework.box.displays.AlexandriaDesktopDisplay;

public class HomePageAction extends PageAction {

	public AlexandriaFrameworkBox box;


	public HomePageAction() { super("alexandria"); }

	public String execute() {
		return super.template("homePage");
	}

	public io.intino.konos.alexandria.foundation.activity.displays.Soul prepareSoul(io.intino.konos.alexandria.foundation.activity.services.push.ActivityClient client) {
	    return new io.intino.konos.alexandria.foundation.activity.displays.Soul(session) {
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