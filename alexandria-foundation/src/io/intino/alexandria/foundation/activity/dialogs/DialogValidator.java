package io.intino.alexandria.foundation.activity.dialogs;

import io.intino.alexandria.foundation.activity.model.Dialog;

import java.util.ArrayList;
import java.util.List;

public interface DialogValidator {
    DialogValidator.Result validate(Dialog.Tab.Input input);

    class Result {
        private boolean status;
        private String message;
        private List<Dialog.Tab.Input> modifiedInputs = new ArrayList<>();

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
