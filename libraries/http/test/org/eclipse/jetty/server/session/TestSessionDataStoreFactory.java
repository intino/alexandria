package org.eclipse.jetty.server.session;

import org.eclipse.jetty.session.AbstractSessionDataStoreFactory;
import org.eclipse.jetty.session.SessionDataStore;
import org.eclipse.jetty.session.SessionManager;

public class TestSessionDataStoreFactory extends AbstractSessionDataStoreFactory {

	@Override
	public SessionDataStore getSessionDataStore(SessionManager sessionManager) throws Exception {
		TestSessionDataStore store = new TestSessionDataStore();
		store.setSavePeriodSec(getSavePeriodSec());
		return store;
	}
}
