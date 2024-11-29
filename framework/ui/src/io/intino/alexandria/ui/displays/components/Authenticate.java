package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SignEvent;
import io.intino.alexandria.ui.displays.events.SignListener;
import io.intino.alexandria.ui.displays.notifiers.AuthenticateNotifier;

public class Authenticate<DN extends AuthenticateNotifier, B extends Box> extends AbstractAuthenticate<DN, B> {
	private SignListener executeListener;

	public Authenticate(B box) {
		super(box);
	}

	public void onExecute(SignListener listener) {
		this.executeListener = listener;
	}

	public void info(String value) {
		executeListener.accept(new SignEvent(this, null, null, null));
	}

	public void execute() {
		// TODO
	}

}