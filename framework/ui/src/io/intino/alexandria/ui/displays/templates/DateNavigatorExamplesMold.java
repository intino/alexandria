package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.templates.AbstractDateNavigatorExamplesMold;

public class DateNavigatorExamplesMold extends AbstractDateNavigatorExamplesMold<UiFrameworkBox> {

	public DateNavigatorExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		dateNavigator1.onSelect(e -> notifyUser("Date selected " + e.option(), UserMessage.Type.Info));
		dateNavigator1.onSelectScale(e -> notifyUser("Scale selected " + e.option(), UserMessage.Type.Info));
	}
}