package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.AbstractBaseDashboard;
import io.intino.alexandria.ui.displays.notifiers.BaseDashboardNotifier;
import io.intino.alexandria.ui.displays.notifiers.BaseTextNotifier;

import java.util.HashMap;
import java.util.Map;

public class BaseDashboard<DN extends BaseDashboardNotifier, B extends Box> extends AbstractBaseDashboard<DN, B> {
    private java.util.Map<String, Object> parameterMap = new HashMap<>();
    private boolean adminMode = false;

    public BaseDashboard(B box) {
        super(box);
    }

    public Map<String, Object> parameters() {
        return parameterMap;
    }

    public BaseDashboard<DN, B> parameters(java.util.Map<String, Object> parameters) {
        this.parameterMap = parameters;
        refresh();
        return this;
    }

    public boolean adminMode() {
        return adminMode;
    }

    public BaseDashboard<DN, B> adminMode(boolean value) {
        _adminMode(value);
        return this;
    }

    protected BaseDashboard<DN, B> _adminMode(boolean value) {
        this.adminMode = value;
        return this;
    }

    protected BaseDashboard<DN, B> _add(String parameter, String value) {
        parameterMap.put(parameter, value);
        return this;
    }

}