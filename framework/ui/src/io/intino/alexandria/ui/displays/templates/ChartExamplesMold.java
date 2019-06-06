package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.components.Chart;
import io.intino.alexandria.ui.displays.components.TextEditableCode;

public class ChartExamplesMold extends AbstractChartExamplesMold<AlexandriaUiBox> {
    private boolean chartsLinked = false;

    public ChartExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        linkCharts();
    }

    private void linkCharts() {
        if (chartsLinked) return;
        linkChart(chart1, chart1Editor);
        linkChart(chart2, chart2Editor);
        chartsLinked = true;
    }

    private void linkChart(Chart chart, TextEditableCode textCode) {
        chart.query(textCode.value());
        chart.refresh();
        textCode.onChange(event -> {
            chart.query(event.value());
            chart.refresh();
        });
    }
}