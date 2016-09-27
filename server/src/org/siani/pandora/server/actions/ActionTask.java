package org.siani.pandora.server.actions;

import org.siani.pandora.server.core.Session;
import org.siani.pandora.server.services.BrowserService;
import org.siani.pandora.server.services.ServiceSupplier;
import org.siani.pandora.server.core.Client;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ActionTask<I extends Action.Input, O extends Action.Output> implements Action.Task<I, O> {
	protected final ServiceSupplier serviceSupplier;

	public ActionTask(ServiceSupplier serviceSupplier) {
		this.serviceSupplier = serviceSupplier;
	}

	@Override
	public void before(I input, O output) {
		linkTask(clientOf(input));
		serviceSupplier.service(BrowserService.class).url(baseUrlOf(input));
	}

	@Override
	public void after(I input, O output) {
		unlinkTask();
	}

	private void linkTask(Client client) {
		if (client != null)
			browserService().linkToThread(client);
	}

	private void unlinkTask() {
		browserService().unlinkFromThread();
	}

	protected <T extends Client> T clientOf(Action.Input input) {
		return (T) clientOf(input.clientId());
	}

	protected <T extends Client> T clientOf(String id) {
		return serviceSupplier.service(BrowserService.class).getClient(id);
	}

	protected <T extends Session> T sessionOf(Action.Input input) {
		return (T) sessionOf(input.sessionId());
	}

	protected <T extends Session> T sessionOf(String id) {
		return serviceSupplier.service(BrowserService.class).getSession(id);
	}

	protected BrowserService browserService() {
		return serviceSupplier.service(BrowserService.class);
	}

	private URL baseUrlOf(Action.Input input) {
		try {
			return new URL(input.baseUrl());
		} catch (MalformedURLException e) {
			return null;
		}
	}

}
