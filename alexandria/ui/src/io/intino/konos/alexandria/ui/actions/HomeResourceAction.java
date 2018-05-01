package io.intino.konos.alexandria.ui.actions;

import io.intino.konos.alexandria.ui.UIBox;
import io.intino.konos.alexandria.ui.displays.AlexandriaDesktop;
import io.intino.konos.alexandria.ui.displays.Soul;
import io.intino.konos.alexandria.ui.services.push.UIClient;
import io.intino.konos.alexandria.ui.spark.actions.AlexandriaResourceAction;

public class HomeResourceAction extends AlexandriaResourceAction {

	public UIBox box;

	public HomeResourceAction() { super("alexandria"); }

	public String execute() {
		return super.template("homePage");
	}

	public Soul prepareSoul(UIClient client) {
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