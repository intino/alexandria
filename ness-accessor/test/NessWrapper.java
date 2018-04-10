import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.Ness;
import io.intino.konos.datalake.jms.JMSTank;
import org.junit.Test;

public class NessWrapper {

	@Test
	public void shouldStartNess() throws Exception {
		final Ness ness = new Ness("failover:(tcp://localhost:61616)","ness", "ness", "ness");
//		ness.start();
		final Datalake.Tank tank = ness.add("");
		Thread.sleep(10000);
	}
}
