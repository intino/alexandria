package io.intino.konos.framework.functions;

import io.intino.ness.inl.Message;

public interface TextMapper extends MessageFunction {
	Message map(String input);
}
