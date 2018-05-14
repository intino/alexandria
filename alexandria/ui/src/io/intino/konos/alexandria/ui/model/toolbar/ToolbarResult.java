package io.intino.konos.alexandria.ui.model.toolbar;

import io.intino.konos.alexandria.ui.model.AbstractResult;

public class ToolbarResult extends AbstractResult<ToolbarResult.Refresh> {

	public static ToolbarResult none() {
		return new ToolbarResult().refresh(ToolbarResult.Refresh.None);
	}

	public static ToolbarResult container() {
		return new ToolbarResult().refresh(ToolbarResult.Refresh.Container);
	}

	public enum Refresh {
		None, Container
	}

}