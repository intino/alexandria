package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ChartInfo;
import io.intino.alexandria.ui.displays.components.chart.ChartEngine;
import io.intino.alexandria.ui.displays.components.chart.Dataframe;
import io.intino.alexandria.ui.displays.components.chart.DataframeLoader;
import io.intino.alexandria.ui.displays.components.chart.Output;
import io.intino.alexandria.ui.displays.notifiers.ChartNotifier;
import io.intino.alexandria.ui.utils.UrlUtil;

import java.net.URL;

public class Chart<DN extends ChartNotifier, B extends Box> extends AbstractChart<B> {
    private Dataframe input;
    private String query;
    private ChartEngine engine = new ChartEngine();
    private Output output = Output.Html;
    private URL serverUrl = null;

    public Chart(B box) {
        super(box);
    }

    public Dataframe input() {
        return input;
    }

    public Chart<DN, B> input(Dataframe input) {
        this.input = input;
        this.refresh();
        return this;
    }

    public Chart<DN, B> query(String query) {
        _query(query);
        refresh();
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

    protected Chart<DN, B> _input(DataframeLoader loader) {
        this.input = loader.load();
        return this;
    }

    protected Chart<DN, B> _query(String query) {
        this.query = query;
        return this;
    }

    protected Chart<DN, B> _output(String output) {
        this.output = Output.valueOf(output);
        return this;
    }

    protected Chart<DN, B> _serverUrl(String url) {
        this.serverUrl = UrlUtil.toURL(url);
        return this;
    }

    private String execute() {
        return engine.execute(serverUrl, input, query, output);
    }

}