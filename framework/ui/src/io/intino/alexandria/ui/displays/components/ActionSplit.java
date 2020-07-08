package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.actionable.ExecuteEvent;
import io.intino.alexandria.ui.displays.events.actionable.ExecuteListener;
import io.intino.alexandria.ui.displays.notifiers.ActionSplitNotifier;

public class ActionSplit<DN extends ActionSplitNotifier, B extends Box> extends AbstractActionSplit<DN, B> {
	private java.util.List<String> options;
	private String option;
	private ExecuteListener executeListener;

	public ActionSplit(B box) {
		super(box);
	}

	public ActionSplit<DN, B> onExecute(ExecuteListener listener) {
		this.executeListener = listener;
		return this;
	}

	public String option() {
		return this.option;
	}

	public ActionSplit<DN, B> execute(String option) {
		_option(option);
		notifyExecute();
		return this;
	}

	protected ActionSplit<DN, B> _options(java.util.List<String> options) {
		this.options = options;
		return this;
	}

	protected ActionSplit<DN, B> _option(String option) {
		this.option = option;
		return this;
	}

	private void notifyExecute() {
		notifier.refreshOption(option);
		if (this.executeListener == null) return;
		this.executeListener.accept(new ExecuteEvent(this, option));
	}

}