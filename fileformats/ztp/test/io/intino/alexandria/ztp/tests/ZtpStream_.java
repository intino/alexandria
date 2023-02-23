package io.intino.alexandria.ztp.tests;

import io.intino.alexandria.ztp.Tuple;
import io.intino.alexandria.ztp.ZtpStream;
import org.junit.Test;

import java.time.Instant;

import static io.intino.alexandria.ztp.Tuple.TUPLE_SEPARATOR;
import static org.junit.Assert.assertEquals;

public class ZtpStream_ {

	public static final Instant NOW = Instant.now();
	static {
		System.out.println(NOW);
	}

	@Test
	public void should_read_tuples() {
		String tuples = "VID_PLA_VIS:videometry\tname\tVideometría Playa Las Vistas\tdashboard\t" + NOW +
						"\nVID_PLA_VIS:videometry\tplace\t28.05056,-16.72095\tdashboard\t" + NOW +
						"\nVID_PLA_VIS:videometry\tzones\tZON_LV_O4,ZON_LV_O1,ZON_LV_P2,ZON_LV_P1,ZON_LV_O3,ZON_LV_O2\tdashboard\t" + NOW +
						"\nVID_PLA_VIS:videometry\tenabled\ttrue\tdashboard\t" + NOW;

		assertEquals(
				new Tuple(("VID_PLA_VIS:videometry\tplace\t28.05056,-16.72095\tdashboard\t" + NOW).split(TUPLE_SEPARATOR, -1)),
				ZtpStream.of(tuples).filter(t -> t.get(1).equals("place")).findFirst().get());
	}
}
