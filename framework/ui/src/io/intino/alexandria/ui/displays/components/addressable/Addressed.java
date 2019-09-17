package io.intino.alexandria.ui.displays.components.addressable;

import java.util.function.Function;

public interface Addressed<T extends Addressable> {
	T address(Function<String, String> resolver);
}
