package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.foundation.activity.displays.ActivityDisplay;
import io.intino.alexandria.framework.box.displays.notifiers.DesktopDisplayNotifier;

public class DesktopDisplay extends ActivityDisplay<DesktopDisplayNotifier> {
	public DesktopDisplay(Box box) {
		super(box);
	}
}