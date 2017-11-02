package io.intino.konos.alexandria.framework.box.model.layout.options;

import io.intino.konos.alexandria.framework.box.model.layout.ElementOption;

import java.util.ArrayList;
import java.util.List;

public class Group extends ElementOption {
    private String label;
    private Mode mode;
    private List<ElementOption> optionList = new ArrayList<>();

    public String label() {
        return label;
    }

    public Group label(String label) {
        this.label = label;
        return this;
    }

    public Mode mode() {
        return mode;
    }

    public Group mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public List<ElementOption> options() {
        return optionList;
    }

    public Group add(ElementOption option) {
        this.optionList.add(option);
        return this;
    }

    public enum Mode {
        Expanded, Collapsed
    }
}
