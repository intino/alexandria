import org.siani.teseo.jmx.JMXClient;

import java.util.Collections;

public class JMXClientTest {

	public static void main(String[] args) {
		JMXClient localhost = new JMXClient("localhost", 9999);
		JMXClient.JMXConnection connection = localhost.connect(Collections.singletonMap("SimpleStandard", null));
		SimpleStandardMBean bean = connection.mBean(SimpleStandardMBean.class);
		System.out.println(bean.getState());
	}
}
