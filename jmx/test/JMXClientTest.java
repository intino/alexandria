import org.siani.teseo.jmx.JMXClient;
import teseo.consul.jmx.ConsulManagerMBean;

import java.io.IOException;
import java.util.List;

public class JMXClientTest {

	public static void main(String[] args) {
		JMXClient localhost = new JMXClient("localhost", 9999);
		JMXClient.JMXConnection connection = localhost.connect();
		SimpleStandardMBean bean = connection.mBean(SimpleStandardMBean.class);
		System.out.println(bean.getState());
	}


	public static void mains(String[] args) throws IOException {
		final List<String> servers = JMXClient.allJMXLocalURLs();
		JMXClient localhost = new JMXClient(servers.get(0));
		JMXClient.JMXConnection connection = localhost.connect();
		ConsulManagerMBean bean = connection.mBean(ConsulManagerMBean.class);
		assert bean != null;
		bean.executeDeploy();
	}
}
