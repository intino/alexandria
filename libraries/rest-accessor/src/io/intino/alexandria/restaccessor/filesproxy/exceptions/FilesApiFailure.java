package io.intino.alexandria.restaccessor.filesproxy.exceptions;

import io.intino.alexandria.exceptions.AlexandriaError;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class FilesApiFailure extends Exception implements AlexandriaError {
	private final String reason;

	public FilesApiFailure(String reason) {
		this.reason = reason;
	}

	@Override
	public String code() {
		return "err:faf";
	}

	@Override
	public String label() {
		return reason;
	}

	@Override
	public Map<String, String> parameters() {
		return emptyMap();
	}
}
