package io.intino.konos.alexandria.functions;

import io.intino.ness.inl.Message;
import io.intino.ness.inl.MessageFunction;

public interface MessageMapper extends MessageFunction {
    Message map(Message input);
}
