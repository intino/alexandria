package io.intino.konos.alexandria.ui.model.mold;

import io.intino.konos.alexandria.ui.model.AbstractResult;

public class StampResult extends AbstractResult<StampResult.Refresh> {

	public static StampResult none() {
		return new StampResult().refresh(Refresh.None);
	}

	public static StampResult item() {
		return new StampResult().refresh(Refresh.Item);
	}

	public static StampResult container() {
		return new StampResult().refresh(Refresh.Container);
	}

	public enum Refresh {
		None, Item, Container
	}

}
