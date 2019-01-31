package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

import java.util.function.Consumer;

public class Value<B extends Box> extends AbstractValue<B> {
	private String value;
	private Consumer<String> changeListener = null;

	public Value(B box) {
        super(box);
    }

    public Value<B> onChange(Consumer<String> consumer) {
    	this.changeListener = consumer;
    	return this;
	}

	public String value() {
		return value;
	}

	public void update(String value) {
		this.value = value;
		notifier.update(value);
		if (changeListener != null) changeListener.accept(value);
	}

}