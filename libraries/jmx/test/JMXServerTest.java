import io.intino.alexandria.jmx.JMXServer;

import java.util.Collections;

public class JMXServerTest {

	public static void main(String[] args) {
		JMXServer server = new JMXServer(Collections.singletonMap("SimpleStandard", null));
		server.init();
	}
}
