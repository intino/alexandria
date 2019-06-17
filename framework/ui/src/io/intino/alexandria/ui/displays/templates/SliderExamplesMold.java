package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;

public class SliderExamplesMold extends AbstractSliderExamplesMold<AlexandriaUiBox> {

    public SliderExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        slider1.add(new Ordinal() {
            @Override
            public String name() {
                return "ordinal";
            }

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
        slider1.onChange(event -> slider1.notifyUser(String.format("Se ha seleccionado el valor %d", (Long)event.value()), UserMessage.Type.Info));
        slider1.refresh();
    }
}