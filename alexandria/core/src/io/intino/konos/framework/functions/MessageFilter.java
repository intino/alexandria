package io.intino.konos.framework.functions;

import io.intino.ness.inl.Message;

public interface MessageFilter extends MessageFunction {
    boolean filter(Message message);
}
