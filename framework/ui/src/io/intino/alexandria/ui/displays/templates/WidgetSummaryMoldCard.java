package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Widget;

public class WidgetSummaryMoldCard extends AbstractWidgetSummaryMoldCard<UiFrameworkBox> {

	public WidgetSummaryMoldCard(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void refresh() {
		if (this.item() == null) return;
		Widget item = item();
		updateTitle();
		description.value(translate(item.description()));
		facets.clear();
		facets.addAll(item.facets());
	}

	private void updateTitle() {
		String simpleName = item().getClass().getSimpleName().replace("Widget", "");
		title.title(translate(simpleName));
		title.path("/widgets/" + simpleName.toLowerCase() + "/");
	}

}