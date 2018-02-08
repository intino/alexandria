package io.intino.konos.alexandria.functions;

import io.intino.ness.inl.Message;

public interface MessageMapper extends MessageFunction {
    Message map(Message input);
}
