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
	public void toScale() {
		assertEquals(Timetag.of("2025"), Timetag.of("202506050023").toScale(Scale.Year));
		assertEquals(Timetag.of("202501010000"), Timetag.of("2025").toScale(Scale.Minute));
		assertEquals(Timetag.of("20250101"), Timetag.of("2025").toScale(Scale.Day));
		assertEquals(Timetag.of("2025010119"), Timetag.of("202501011959").toScale(Scale.Hour));
	}

	@Test
	public void comparison() {
		assertTrue(Timetag.of("20250514").isBefore(Timetag.of("20250515")));
        assertFalse(Timetag.of("20250514").isBefore(Timetag.of("20250513")));
		assertFalse(Timetag.of("202505140000").isBefore(Timetag.of("20250514")));
		assertFalse(Timetag.of("20250514").isBefore(Timetag.of("202505140000")));
		assertTrue(Timetag.of("2025").isAfter(Timetag.of("202305140000")));
		assertFalse(Timetag.of("202305140000").isAfter(Timetag.of("2025")));
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
