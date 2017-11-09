import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import io.intino.konos.consul.jmx.ConsulManagerMBean;
import io.intino.konos.jmx.JMXClient;

import java.io.IOException;
import java.util.Map;

public class JMXClientTest {

	public static void main(String[] args) throws IOException {
		JMXClient localhost = new JMXClient("localhost", 9999);
		JMXClient.JMXConnection connection = localhost.connect();
		SimpleStandardMBean bean = connection.mBean(SimpleStandardMBean.class);
		System.out.println(bean.getState());
	}


	public static void mains(String[] args) throws IOException, AgentLoadException, AgentInitializationException {
		final Map<String, String> servers;
		servers = JMXClient.allJMXLocalURLs();
		JMXClient localhost = new JMXClient(servers.values().iterator().next());
		JMXClient.JMXConnection connection = localhost.connect();
		ConsulManagerMBean bean = connection.mBean(ConsulManagerMBean.class);
		assert bean != null;
		bean.executeDeploy();
	}
}
