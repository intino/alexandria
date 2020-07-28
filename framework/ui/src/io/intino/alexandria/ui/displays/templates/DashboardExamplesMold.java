package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.drivers.shiny.Driver;

public class DashboardExamplesMold extends AbstractDashboardExamplesMold<UiFrameworkBox> {

    public DashboardExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        initShinyDashboard();
        initMetabaseDashboard();
    }

    private void initShinyDashboard() {
        shinyDashboard.adminMode(true);
        shinyDashboard.driver(new Driver("http://10.13.13.37:3838"));
        shinyDashboard.refresh();
    }

    private void initMetabaseDashboard() {
        dashboard.onChange(e -> {
            metabaseDashboard.dashboard(((Double)e.value()).intValue());
            metabaseDashboard.refresh();
        });
        secretKey.onChange(e -> {
            metabaseDashboard.secretKey(e.value());
            metabaseDashboard.refresh();
        });
        metabaseDashboard.refresh();
    }
}