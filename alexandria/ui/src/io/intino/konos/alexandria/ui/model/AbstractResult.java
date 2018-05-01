package io.intino.konos.alexandria.ui.model;

public abstract class AbstractResult<R> {
	private R refresh;
	private String message = null;

	public R refresh() {
		return refresh;
	}

	public <S extends AbstractResult> S refresh(R refresh) {
		this.refresh = refresh;
		return (S) this;
	}

	public String message() {
		return message;
	}

	public <S extends AbstractResult> S message(String message) {
		this.message = message;
		return (S) this;
	}
}
