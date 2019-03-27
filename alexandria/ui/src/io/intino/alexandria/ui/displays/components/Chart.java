package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ChartInfo;
import io.intino.alexandria.ui.displays.components.chart.ChartEngine;
import io.intino.alexandria.ui.displays.components.chart.ChartMode;
import io.intino.alexandria.ui.displays.components.chart.ChartSheet;

import java.net.URL;

public class Chart<B extends Box> extends AbstractChart<B> {
    private ChartSheet input;
    private String code;
    private ChartEngine engine = new ChartEngine();
    private ChartMode mode = ChartMode.Html;

    public Chart(B box) {
        super(box);
    }

    public ChartSheet input() {
        return input;
    }

    public Chart<B> input(URL source) {
        this.input = ChartSheet.fromSource(source);
        return this;
    }

    public Chart<B> code(String code) {
        this.code = code;
        return this;
    }

    public Chart<B> mode(String mode) {
        this.mode = ChartMode.valueOf(mode);
        return this;
    }

    public Chart<B> update(ChartSheet sheet) {
        this.input = sheet;
        this.refresh();
        return this;
    }

    public Chart<B> engine(ChartEngine engine) {
        this.engine = engine;
        return this;
    }

    @Override
    public void refresh() {
        try {
            ChartSheet input = input();
            if (input == null) return;
            notifier.showLoading();
            String result = execute();
            notifier.refresh(new ChartInfo().mode(mode.name()).config(result));
        }
        catch (RuntimeException ex) {
            notifier.refreshError(ex.getMessage());
        }
    }

    private String execute() {
        return engine.execute(input, code, mode);
    }

}