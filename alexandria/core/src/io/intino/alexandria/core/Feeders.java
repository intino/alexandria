package io.intino.alexandria.core;

import java.util.ArrayList;
import java.util.List;

public class Feeders {

	private static Feeders instance = null;
	private List<Feeder> feeders = new ArrayList<>();


	public static Feeders get() {
		if (instance == null) return instance = new Feeders();
		return instance;
	}

	public <T extends Feeder> T feederFor(List<String> eventTypes) {
		return (T) feeders.stream().filter(feeder -> feeder.fits(eventTypes)).findFirst().orElse(null);
	}

	public void register(Feeder feeder) {
		feeders.add(feeder);
	}

}
