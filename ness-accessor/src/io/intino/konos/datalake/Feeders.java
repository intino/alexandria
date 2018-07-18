package io.intino.konos.datalake;

import java.util.ArrayList;
import java.util.List;

public abstract class Feeders {

	private static List<Feeder> feeders = new ArrayList<>();

	public static <T extends Feeder> T feederFor(List<String> eventTypes) {
		return (T) feeders.stream().filter(feeder -> feeder.fits(eventTypes)).findFirst().orElse(null);
	}

	public static void register(Feeder feeder) {
		feeders.add(feeder);
	}

}
