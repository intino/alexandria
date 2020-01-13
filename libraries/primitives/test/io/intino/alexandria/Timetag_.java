package io.intino.alexandria;

import java.time.LocalDate;

public class Timetag_ {

	@org.junit.Test
	public void name() {
		Timetag timetag = new Timetag(LocalDate.now(), Scale.Minute);
	}
}
