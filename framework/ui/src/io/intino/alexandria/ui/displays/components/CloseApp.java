package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.CloseAppNotifier;

public class CloseApp<DN extends CloseAppNotifier, B extends Box> extends AbstractCloseApp<DN, B> {

	public CloseApp(B box) {
        super(box);
    }

	public void execute() {
		notifier.close();
	}

}