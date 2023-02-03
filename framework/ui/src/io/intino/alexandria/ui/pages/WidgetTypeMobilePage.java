package io.intino.alexandria.ui.pages;

import io.intino.alexandria.exceptions.*;
import java.time.*;
import java.util.*;
import io.intino.alexandria.ui.displays.templates.*;

public class WidgetTypeMobilePage extends AbstractWidgetTypeMobilePage {
	public String type;

	public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {
		return new io.intino.alexandria.ui.Soul(session) {
			@Override
			public void personify() {
				WidgetTypeTemplate component = new WidgetTypeTemplate(box);
				register(component);
				component.init();
			}
		};
	}
}