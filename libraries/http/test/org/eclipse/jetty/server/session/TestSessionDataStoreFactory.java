package org.eclipse.jetty.server.session;

public class TestSessionDataStoreFactory extends AbstractSessionDataStoreFactory {

	/**
	 * @see SessionDataStoreFactory#getSessionDataStore(SessionHandler)
	 */
	@Override
	public SessionDataStore getSessionDataStore(SessionHandler handler) throws Exception {
		TestSessionDataStore store = new TestSessionDataStore();
		store.setSavePeriodSec(getSavePeriodSec());
		return store;
	}
}
