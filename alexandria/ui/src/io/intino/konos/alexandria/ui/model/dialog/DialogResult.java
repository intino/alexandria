package io.intino.konos.alexandria.ui.model.dialog;

import io.intino.konos.alexandria.ui.model.AbstractResult;

public class DialogResult extends AbstractResult<DialogResult.Refresh> {

	public static DialogResult none() {
		return new DialogResult().refresh(Refresh.None);
	}

	public static DialogResult item() {
		return new DialogResult().refresh(Refresh.Item);
	}

	public static DialogResult container() {
		return new DialogResult().refresh(Refresh.Container);
	}

	public enum Refresh {
		None, Item, Container
	}

}
