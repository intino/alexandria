package io.intino.alexandria.ui.displays.blocks;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.displays.PropertiesDisplay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateBlock extends AbstractDateBlock<UiFrameworkBox> {

    public DateBlock(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        dateA.update(Instant.now());
        addPropertiesDisplay();
    }

    private void addPropertiesDisplay() {
        PropertiesDisplay display = new PropertiesDisplay(box());
        display.properties(propertyList());
        dateProperties.set(display);
        dateProperties.refresh();
    }

    private List<Property> propertyList() {
        return new ArrayList<Property>() {{
           add(property("format", Property.Type.Text, "used to...", "dd/MM/YYYY"));
           add(property("mode", Property.Type.Word, "used to...", "FromNow", "ToNow"));
        }};
    }

    private Property property(String name, Property.Type type, String description, String... values) {
        return new Property().name(name).type(type).description(description).values(Arrays.asList(values));
    }
}