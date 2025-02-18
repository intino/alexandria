package io.intino.alexandria.http.server;

import io.intino.alexandria.Resource;

import java.util.List;

public interface AlexandriaHttpResourceProvider {
	List<Resource> resources();
	Resource resource(String name);
}
