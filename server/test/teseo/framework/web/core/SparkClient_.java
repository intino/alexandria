package teseo.framework.web.core;

import teseo.framework.core.Client;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SparkClient_ {

	@Test
	public void should_get_info_from_client() {
		Client client = new SparkClient(createSession());

		assertThat(client.id(), is("1"));
		assertThat(client.sessionId(), is("123"));
		assertThat(client.language(), is("es"));
	}

	private Session createSession() {
		URI uri = URI.create("ws://localhost:8080?id=1&session=123&language=es");
		UpgradeRequest upgradeRequest = mock(UpgradeRequest.class);
		when(upgradeRequest.getRequestURI()).thenReturn(uri);
		Session session = mock(Session.class);
		when(session.getUpgradeRequest()).thenReturn(upgradeRequest);
		return session;
	}
}
