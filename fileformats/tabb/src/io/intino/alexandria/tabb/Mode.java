package io.intino.alexandria.tabb;

import java.util.Collections;
import java.util.List;

public class Mode {
	final List<String> features;

	public Mode(List<String> features) {
		this.features = features;
	}

	public List<String> features() {
		return Collections.unmodifiableList(features);
	}
}
