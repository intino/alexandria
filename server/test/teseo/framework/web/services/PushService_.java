package teseo.framework.web.services;

import teseo.framework.actions.AdapterProxy;
import teseo.framework.core.Client;
import teseo.framework.core.Message;
import teseo.framework.core.Session;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.mockito.Mockito.*;

public class PushService_ {

    private PushService service;

    @Before
    public void setUp() {
        service = new PushService(mock(AdapterProxy.class), mock(BrowserService.class));
    }

    @Test
    public void should_send_notification_to_only_one_session() throws Exception {
        List<Client> openClients = openConnections(2);

        service.pushToClient(openClients.get(0), new Message("Hello world"));

        verifyMessageReceived(openClients.get(0));
        verifyNoMoreInteractions(openClients.get(1));
    }

    @Test
    public void should_send_notification_to_all_sessions_of_user() throws Exception {
        List<Client> openClients = openConnections(3);

        service.pushToSession(sessionWithClients(openClients.subList(0, 2)), new Message("Hello world"));

        openClients.stream().limit(2).forEach(this::verifyMessageReceived);
        verifyNoMoreInteractions(openClients.get(2));
    }

    @Test
    public void should_broadcast_message_to_all() throws Exception {
        List<Client> openClients = openConnections(3);

        service.pushBroadcast(new Message("Hello world"));

        openClients.stream().forEach(this::verifyMessageReceived);
    }

    private void verifyMessageReceived(Client client) {
        verify(client, times(1)).send("{\"name\":\"Hello world\",\"parameters\":{}}");
    }

    private List<Client> openConnections(int numberOfConnections) {
        List<Client> clients = range(0, numberOfConnections).mapToObj(i -> mock(Client.class)).collect(toList());
        clients.forEach(service::onOpen);
        return clients;
    }

    private Session sessionWithClients(List<Client> clients) {
        Session session = new Session("any");
        clients.stream().forEach(session::add);
        return session;
    }
}
