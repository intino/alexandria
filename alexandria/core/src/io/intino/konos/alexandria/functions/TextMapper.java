package io.intino.konos.alexandria.functions;

import io.intino.ness.inl.Message;
import io.intino.ness.inl.MessageFunction;

public interface TextMapper extends MessageFunction {
	Message map(String input);
}
