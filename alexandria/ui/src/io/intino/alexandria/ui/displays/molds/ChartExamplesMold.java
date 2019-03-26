package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;

public class ChartExamplesMold extends AbstractChartExamplesMold<UiFrameworkBox> {

    public ChartExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        chart1.refresh();
    }
}