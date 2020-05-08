package io.intino.alexandria.message.exceptions;

import java.util.ArrayList;
import java.util.List;

public abstract class InlException extends Exception {
	protected List<InlError> errors = new ArrayList<>();

	public InlException add(InlError error) {
		errors.add(error);
		return this;
	}

	public List<? extends InlError> errors() {
		return errors;
	}

}
