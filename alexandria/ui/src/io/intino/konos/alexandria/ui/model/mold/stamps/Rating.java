package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class Rating extends Stamp<Double> {
	private String ratingIcon;
	private int max;

	public String ratingIcon() {
		return this.ratingIcon;
	}

	public Rating ratingIcon(String ratingIcon) {
		this.ratingIcon = ratingIcon;
		return this;
	}

	public int max() {
		return max;
	}

	public Rating max(int max) {
		this.max = max;
		return this;
	}

	@Override
	public Double objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
