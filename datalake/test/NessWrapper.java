import io.intino.konos.datalake.Ness;
import org.junit.Test;

public class NessWrapper {

	@Test
	public void shouldStartNess() throws Exception {
		final Ness ness = new Ness("failover:(tcp://localhost:61616)","ness", "ness", "ness");
		ness.start();
		final Ness.Tank tank = ness.tank("");
		Thread.sleep(10000);
	}
}
