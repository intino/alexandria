package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.RangeValue;
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

        slider1.add(ordinal());
        slider1.onChange(event -> slider1.notifyUser(String.format("Se ha seleccionado el valor %d", (Long)event.value()), UserMessage.Type.Info));
        slider1.refresh();

        slider2.add(ordinal());
        slider2.onChange(event -> slider2.notifyUser(String.format("Se ha seleccionado el valor %d", (Long)event.value()), UserMessage.Type.Info));
        slider2.refresh();

        slider3.onChange(event -> slider3.notifyUser(String.format("Se ha seleccionado el valor %s", event.value().toString()), UserMessage.Type.Info));

        slider4.add(ordinal());
        slider4.onChange(event -> slider4.notifyUser(String.format("Se ha seleccionado el valor %d", (Long)event.value()), UserMessage.Type.Info));
        slider4.refresh();

        slider5.add(ordinal());
        slider5.onChange(event -> slider5.notifyUser(String.format("Se ha seleccionado el valor %d", (Long)event.value()), UserMessage.Type.Info));
        slider5.refresh();

        slider6.add(ordinal());
        slider6.marks(0L, 10L, 100L);
        slider6.onChange(event -> slider6.notifyUser(String.format("Se ha seleccionado el valor %d", (Long)event.value()), UserMessage.Type.Info));
        slider6.refresh();

        slider7.add(ordinal());
        slider7.onChange(event -> slider7.notifyUser(String.format("Se ha seleccionado el valor %d", (Long)event.value()), UserMessage.Type.Info));
        slider7.refresh();

        slider8.onChange(event -> slider8.notifyUser(String.format("Se ha seleccionado el rango %d - %d", ((RangeValue)event.value()).from(), ((RangeValue)event.value()).to()), UserMessage.Type.Info));
        slider8.refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        slider1.refresh();
        slider2.refresh();
        slider3.refresh();
        slider4.refresh();
        slider5.refresh();
        slider6.refresh();
        slider7.refresh();
        slider8.refresh();
    }

    private Ordinal ordinal() {
        return new Ordinal() {
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
            public Formatter formatter(String language) {
                return value -> value + "%";
            }
        };
    }
}