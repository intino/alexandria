package io.intino.alexandria;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class Timetag_ {

	@Test
	public void isBetween() {
		Timetag timetag = new Timetag("202203");
		assertTrue(timetag.isBetween("202201", "202204"));
		assertFalse(timetag.isBetween("202204", "202208"));
		assertTrue(timetag.isBetween("202203", "202203"));
	}


	@Test
	public void datetime() {
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		Timetag timetag = new Timetag(now, Scale.Minute);
		assertEquals(now, timetag.datetime());
		assertEquals(now.toInstant(ZoneOffset.UTC), timetag.instant());
		assertEquals(now.toLocalDate(), timetag.date());
	}

	@Test
	public void compare() {
		assertTrue(Timetag.now().isAfter(Timetag.now().previous()));
		assertTrue(Timetag.now().isBefore(Timetag.now().next()));
		assertEquals(Timetag.now(), Timetag.now());
		assertEquals(Timetag.today(), Timetag.today().previous().next());
		assertFalse(Timetag.now().isAfter(Timetag.now()));
		assertFalse(Timetag.now().isBefore(Timetag.now()));
		assertEquals(Timetag.now(), Timetag.now());
		assertEquals(Timetag.today(), Timetag.today().previous().next());
	}

	@Test
	public void iterate() {
		Timetag last;

		assertEquals(Timetag.of("20230101"), Timetag.range("20230101", "20230101").findFirst().get());

		last = null;
		for(Timetag t : (Iterable<Timetag>)() -> Timetag.range("20230101", "20230201").iterator()) {
			if(last != null) assertTrue(last.isBefore(t));
			last = t;
		}

		last = null;
		for(Timetag t : (Iterable<Timetag>)() -> Timetag.range("20230201", "20230101").iterator()) {
			if(last != null) assertTrue(last.isAfter(t));
			last = t;
		}
	}
}
