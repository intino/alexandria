package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.Chart;
import io.intino.alexandria.ui.displays.components.TextEditableCode;

public class ChartExamplesMold extends AbstractChartExamplesMold<UiFrameworkBox> {

    public ChartExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        linkChart(chart1, chart1Editor);
        linkChart(chart2, chart2Editor);
    }

    private void linkChart(Chart chart, TextEditableCode textCode) {
        chart.code(textCode.value());
        chart.refresh();
        textCode.onChange(event -> {
            chart.code(event.value());
            chart.refresh();
        });
    }
}