package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.Step;

public class StepEvent extends Event {
	private final Step<?, ?> step;

	public StepEvent(Display<?, ?> sender, Step<?, ?> step) {
		super(sender);
		this.step = step;
	}

	public Step<?, ?> step() {
		return step;
	}

}
