package io.intino.alexandria.ui.displays.templates;

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
        updateTextPanel();
    }

    private void updateTextPanel() {
        title.update("Alexandria widgets");
        valueA.update("abcd");
        valueB.update("efgh");
        valueC.update("ijkl");
//        valueACode.update("<Otro></Otro>");
        valueC.onChange((e) -> System.out.println((String) e.value()));
        int[] counter = new int[1];

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                valueC.update("valor C " + counter[0]);
                counter[0]++;
            }
        }, 1000, 1000);
    }
}