package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Dialog;

import java.util.List;

public interface DialogSource {
    List<String> options(Dialog.Tab.Input input);
}
