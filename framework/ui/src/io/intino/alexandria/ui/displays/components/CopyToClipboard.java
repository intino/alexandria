package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.actionable.CopyEvent;
import io.intino.alexandria.ui.displays.events.actionable.CopyListener;
import io.intino.alexandria.ui.displays.notifiers.CopyToClipboardNotifier;

public class CopyToClipboard<DN extends CopyToClipboardNotifier, B extends Box> extends AbstractCopyToClipboard<DN, B> {
	private String text;
	private CopyListener copyListener;

	public CopyToClipboard(B box) {
        super(box);
    }

	public CopyToClipboard<DN, B> onCopy(CopyListener listener) {
		this.copyListener = listener;
		return this;
	}

	public CopyToClipboard<DN, B> text(String text) {
		_text(text);
		return this;
	}

	protected CopyToClipboard<DN, B> _text(String text) {
		this.text = text;
		return this;
	}

	public void copied(boolean success) {
		if (!success) return;
		notifyUser(translate("Text copied to clipboard"), UserMessage.Type.Info);
		if (copyListener != null) copyListener.accept(new CopyEvent(this, text));
	}

	public void execute() {
		notifier.copy(text);
	}
}