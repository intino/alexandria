package io.intino.konos.framework.functions;

import io.intino.ness.inl.Message;

public interface MessageMapper extends MessageFunction {
    Message map(Message input);
}
