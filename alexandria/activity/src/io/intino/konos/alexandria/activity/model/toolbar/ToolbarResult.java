package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.model.AbstractResult;

public class ToolbarResult extends AbstractResult<ToolbarResult.Refresh> {

	public static ToolbarResult none() {
		return new ToolbarResult().refresh(ToolbarResult.Refresh.None);
	}

	public static ToolbarResult element() {
		return new ToolbarResult().refresh(ToolbarResult.Refresh.Element);
	}

	public enum Refresh {
		None, Element
	}

}