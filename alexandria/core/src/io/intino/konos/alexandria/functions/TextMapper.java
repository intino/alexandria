package io.intino.konos.alexandria.functions;

import io.intino.ness.inl.Message;

public interface TextMapper extends MessageFunction {
	Message map(String input);
}
