package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.rest.pushservice.Client;
import io.intino.konos.alexandria.rest.pushservice.Message;
import io.intino.konos.alexandria.rest.pushservice.PushService;
import io.intino.konos.alexandria.rest.pushservice.Session;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public class MessageCarrier {
    private final Session session;
    private final Client client;
    private final PushService service;

    public MessageCarrier(PushService service, Session session, Client client) {
        this.session = session;
        this.client = client;
        this.service = service;
    }

    public void notifyAll(String message, Map<String, Object> parameters) {
        service.pushBroadcast(new Message(message, parameters));
    }

    public void notifyAll(String message) {
        notifyAll(message, emptyMap());
    }

    public void notifyAll(String message, Object parameterValue) {
        notifyAll(message, singletonMap(message, parameterValue));
    }

    public void notifyAll(String message, String parameter, Object parameterValue) {
        notifyAll(message, singletonMap(parameter, parameterValue));
    }

    public void notifySession(String message, Map<String, Object> parameters) {
        service.pushToSession(session, new Message(message, parameters));
    }

    public void notifySession(String message) {
        notifySession(message, emptyMap());
    }

    public void notifySession(String message, Object parameterValue) {
        notifySession(message, singletonMap(message, parameterValue));
    }

    public void notifySession(String message, String parameter, Object parameterValue) {
        notifySession(message, singletonMap(parameter, parameterValue));
    }

    public void notifyClient(String message, Map<String, Object> parameters) {
        service.pushToClient(client, new Message(message, parameters));
    }

    public void notifyClient(String message) {
        notifyClient(message, emptyMap());
    }

    public void notifyClient(String message, Object parameterValue) {
        notifyClient(message, singletonMap(message, parameterValue));
    }

    public void notifyClient(String message, String parameter, Object parameterValue) {
        notifyClient(message, singletonMap(parameter, parameterValue));
    }

}
