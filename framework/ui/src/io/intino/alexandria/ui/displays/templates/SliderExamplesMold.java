package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;

public class SliderExamplesMold extends AbstractSliderExamplesMold<UiFrameworkBox> {

    public SliderExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        slider1.add(new Ordinal() {
            @Override
            public String label() {
                return "Ordinal";
            }

            @Override
            public int step() {
                return 1;
            }

            @Override
            public String formatter() {
                return null;
            }
        });
        slider1.onChange(new ChangeListener() {
            @Override
            public void accept(ChangeEvent event) {
                slider1.notifyUser(String.format("Se ha seleccionado el valor %d", (int)event.value()), UserMessage.Type.Info);
            }
        });
    }
}