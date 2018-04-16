package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.model.AbstractResult;

public class ToolbarSelectionResult extends AbstractResult<ToolbarSelectionResult.Refresh> {

	public static ToolbarSelectionResult none() {
		return new ToolbarSelectionResult().refresh(ToolbarSelectionResult.Refresh.None);
	}

	public static ToolbarSelectionResult selection() {
		return new ToolbarSelectionResult().refresh(Refresh.Selection);
	}

	public static ToolbarSelectionResult item() {
		return new ToolbarSelectionResult().refresh(Refresh.Item);
	}

	public static ToolbarSelectionResult element() {
		return new ToolbarSelectionResult().refresh(Refresh.Container);
	}

	public enum Refresh {
		None, Selection, Item, Container
	}

}