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
        alexandriaDashboard.adminMode(true);
        alexandriaDashboard.driver(new Driver("http://10.13.13.37:3838"));
        alexandriaDashboard.refresh();
    }
}