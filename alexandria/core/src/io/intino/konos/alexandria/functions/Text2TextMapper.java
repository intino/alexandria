package io.intino.konos.alexandria.functions;

import io.intino.ness.inl.MessageFunction;

public interface Text2TextMapper extends MessageFunction {
    String map(String input);
}
