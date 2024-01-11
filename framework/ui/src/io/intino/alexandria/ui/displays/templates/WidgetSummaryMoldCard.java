package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.documentation.DisplayHelper;

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
		title.title(DisplayHelper.label(item(), this::translate));
		title.path("/widgets/" + DisplayHelper.name(item()).toLowerCase() + "/");
	}

}