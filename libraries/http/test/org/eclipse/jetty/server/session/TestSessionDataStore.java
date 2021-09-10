package org.eclipse.jetty.server.session;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TestSessionDataStore extends AbstractSessionDataStore {
	public Map<String, SessionData> _map = new ConcurrentHashMap<>();
	public AtomicInteger _numSaves = new AtomicInteger(0);

	public final boolean _passivating;

	public TestSessionDataStore() {
		_passivating = false;
	}

	public TestSessionDataStore(boolean passivating) {
		_passivating = passivating;
	}

	@Override
	public boolean isPassivating() {
		return _passivating;
	}

	@Override
	public boolean exists(String id) throws Exception {
		return _map.containsKey(id);
	}

	@Override
	public SessionData doLoad(String id) throws Exception {
		SessionData sd = _map.get(id);
		if (sd == null)
			return null;
		SessionData nsd = new SessionData(id, "", "", System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(), 0);
		nsd.copy(sd);
		return nsd;
	}

	@Override
	public boolean delete(String id) throws Exception {
		return (_map.remove(id) != null);
	}

	@Override
	public void doStore(String id, SessionData data, long lastSaveTime) throws Exception {
		_map.put(id, data);
		_numSaves.addAndGet(1);
	}

	@Override
	public Set<String> doGetExpired(Set<String> candidates) {
		HashSet<String> set = new HashSet<>();
		long now = System.currentTimeMillis();

		for (SessionData d : _map.values()) {
			if (d.getExpiry() > 0 && d.getExpiry() <= now)
				set.add(d.getId());
		}
		return set;
	}
}
