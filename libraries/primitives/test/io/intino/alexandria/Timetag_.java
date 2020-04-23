package io.intino.alexandria;

import org.junit.Test;

import java.time.LocalDate;

public class Timetag_ {

	@Test
	public void name() {
		Timetag timetag = new Timetag(LocalDate.now(), Scale.Minute);
	}
}
