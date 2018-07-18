package io.intino.konos.datalake;

import java.util.List;

public abstract class Feeder {

	public abstract boolean fits(List<String> eventTypes);

}
