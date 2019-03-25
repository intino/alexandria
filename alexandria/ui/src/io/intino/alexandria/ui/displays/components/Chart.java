package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.chart.ChartEngine;
import io.intino.alexandria.ui.displays.components.chart.ChartSheet;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class Chart<B extends Box> extends AbstractChart<B> {
    private ChartSheet input;
    private String code;
    private ChartEngine engine = new ChartEngine();

    public Chart(B box) {
        super(box);
    }

    public ChartSheet input() {
        return input;
    }

    public Chart input(URL source) {
        this.input = ChartSheet.fromSource(source);
        return this;
    }

    public Chart code(String code) {
        this.code = code;
        return this;
    }

    public Chart update(ChartSheet sheet) {
        this.input = sheet;
        this.refresh();
        return this;
    }

    public Chart engine(ChartEngine engine) {
        this.engine = engine;
        return this;
    }

    @Override
    public void refresh() {
        ChartSheet input = input();
        if (input == null) return;
        notifier.refresh(Asset.toResource(baseAssetUrl(), execute()).toUrl().toString());
    }

    private URL execute() {
        return engine.execute(input, code);
    }

}