package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class Rating extends Stamp<Double> {
	private String ratingIcon;

	public String ratingIcon() {
		return this.ratingIcon;
	}

	public Rating ratingIcon(String ratingIcon) {
		this.ratingIcon = ratingIcon;
		return this;
	}

	@Override
	public Double objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
