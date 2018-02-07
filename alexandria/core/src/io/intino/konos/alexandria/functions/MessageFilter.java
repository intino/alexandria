package io.intino.konos.alexandria.functions;

import io.intino.ness.inl.Message;
import io.intino.ness.inl.MessageFunction;

public interface MessageFilter extends MessageFunction {
    boolean filter(Message message);
}
