package io.intino.alexandria.ui.displays;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertiesDisplay extends AbstractPropertiesDisplay<UiFrameworkBox> {
    private List<Property> propertyList = new ArrayList<>();

    public PropertiesDisplay(UiFrameworkBox box) {
        super(box);
    }

    public PropertiesDisplay properties(List<Property> propertyList) {
        this.propertyList = propertyList;
        return this;
    }

    @Override
    public void init() {
        super.init();
        notifier.refresh(propertyList);
    }

}