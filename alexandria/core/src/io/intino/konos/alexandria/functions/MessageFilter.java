package io.intino.konos.alexandria.functions;

import io.intino.ness.inl.Message;

public interface MessageFilter extends MessageFunction {
    boolean filter(Message message);
}
