package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.activity.box.model.Dialog;

import java.util.List;

public interface DialogSource {
    List<String> options(Dialog.Tab.Input input);
}
