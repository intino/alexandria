package io.intino.alexandria.ui.displays.roots;

import io.intino.alexandria.UiFrameworkBox;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends AbstractMain<UiFrameworkBox> {

    public Main(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        title.update("Alexandria widgets");
        value.update("soy un valor de campo");
        valueEditable.update("campo value editable");
        valueEditable.onChange((e) -> System.out.println((String) e.value()));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                menu.select("imageOption");
            }
        }, 2000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                menu.select("textOption");
                value.update("valor 2");
            }
        }, 4000);
    }
}