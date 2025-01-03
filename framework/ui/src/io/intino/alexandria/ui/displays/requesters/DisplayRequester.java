package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.Actionable;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.server.AlexandriaUiManager;
import io.intino.alexandria.ui.server.resources.Resource;
import io.intino.alexandria.ui.services.push.UIClient;

public abstract class DisplayRequester extends Resource {

	public DisplayRequester(AlexandriaUiManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends Display> D display() {
		return display(manager.fromPath("displayId"));
	}

	public <D extends Display> D display(String displayId) {
		if (displayId == null) return null;
		String[] data = manager.fromPath("displayId").split(":");
		UIClient client = manager.currentClient();
		return client == null ? null : client.soul().displayWithId(data[0], data[1], data[2]);
	}

	public String operation() {
		return manager.fromQuery("op");
	}

	@Override
	public void execute() throws AlexandriaException {
		Actionable display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("refresh")) {
			display.refresh();
			return;
		}

		super.execute();
	}

}
