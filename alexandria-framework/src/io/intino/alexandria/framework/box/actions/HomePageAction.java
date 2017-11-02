package io.intino.alexandria.framework.box.actions;

import io.intino.alexandria.foundation.activity.spark.actions.PageAction;
import io.intino.alexandria.framework.box.AlexandriaFrameworkBox;
import io.intino.alexandria.framework.box.displays.DesktopDisplay;

public class HomePageAction extends PageAction {

	public AlexandriaFrameworkBox box;


	public HomePageAction() { super("alexandria"); }

	public String execute() {
		return super.template("homePage");
	}

	public io.intino.alexandria.foundation.activity.displays.Soul prepareSoul(io.intino.alexandria.foundation.activity.services.push.ActivityClient client) {
	    return new io.intino.alexandria.foundation.activity.displays.Soul(session) {
			@Override
			public void personify() {
				DesktopDisplay display = new DesktopDisplay(box);
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