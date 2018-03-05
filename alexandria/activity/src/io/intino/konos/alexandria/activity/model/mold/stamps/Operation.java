package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public abstract class Operation<O> extends Stamp<O> {
	private Mode mode = Mode.Button;
	private String alexandriaIcon = "icons:execute";

	public String alexandriaIcon() {
		return this.alexandriaIcon;
	}

	public enum Mode { Button, Link, Icon }

	public Mode mode() {
		return mode;
	}

	public Operation mode(String mode) {
		return mode(Mode.valueOf(mode));
	}

	public Operation mode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public Operation alexandriaIcon(String alexandriaIcon) {
		this.alexandriaIcon = alexandriaIcon;
		return this;
	}

	@Override
	public O objectValue(Object object, ActivitySession session) {
		return null;
	}
}
