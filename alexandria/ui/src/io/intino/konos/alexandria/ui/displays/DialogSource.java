package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.Dialog;

import java.util.List;

public interface DialogSource {
    List<String> options(Dialog.Tab.Input input);
}
