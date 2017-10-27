package io.intino.konos.server.activity.displays.dialogs;

import io.intino.konos.server.activity.displays.dialogs.model.Dialog;

import java.util.ArrayList;
import java.util.List;

public interface DialogValidator {
    DialogValidator.Result validate(Dialog.Tab.Input input);

    class Result {
        private boolean status;
        private String message;
        private java.util.List<Dialog.Tab.Input> modifiedInputs = new ArrayList<>();

        public Result(boolean status, String message) {
            this.status = status;
            this.message = message;
        }

        public boolean status() {
            return status;
        }

        public String message() {
            return message;
        }

        public List<Dialog.Tab.Input> modifiedInputs() {
            return modifiedInputs;
        }

        public Result modifiedInputs(Dialog.Tab.Input... inputs) {
            modifiedInputs.addAll(java.util.Arrays.asList(inputs));
            return this;
        }
    }
}
