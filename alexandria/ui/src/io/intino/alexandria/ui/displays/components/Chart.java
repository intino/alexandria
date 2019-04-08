package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ChartInfo;
import io.intino.alexandria.ui.displays.components.chart.ChartEngine;
import io.intino.alexandria.ui.displays.components.chart.Dataframe;
import io.intino.alexandria.ui.displays.components.chart.DataframeLoader;
import io.intino.alexandria.ui.displays.components.chart.Output;
import org.eclipse.jetty.websocket.common.frames.DataFrame;

public class Chart<B extends Box> extends AbstractChart<B> {
    private Dataframe input;
    private String query;
    private ChartEngine engine = new ChartEngine();
    private Output output = Output.Html;

    public Chart(B box) {
        super(box);
    }

    public Dataframe input() {
        return input;
    }

    public Chart<B> input(DataframeLoader loader) {
        this.input = loader.load();
        return this;
    }

    public Chart<B> query(String query) {
        this.query = query;
        return this;
    }

    public Chart<B> output(String output) {
        this.output = Output.valueOf(output);
        return this;
    }

    public Chart<B> update(Dataframe input) {
        this.input = input;
        this.refresh();
        return this;
    }

    @Override
    public void refresh() {
        try {
            Dataframe input = input();
            if (input == null) return;
            notifier.showLoading();
            String result = execute();
            notifier.refresh(new ChartInfo().mode(output.name()).config(result));
        }
        catch (RuntimeException ex) {
            notifier.refreshError(ex.getMessage());
        }
    }

    private String execute() {
        return engine.execute(input, query, output);
    }

}