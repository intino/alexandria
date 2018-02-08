package io.intino.konos.alexandria.schema;

import java.util.ArrayList;
import java.util.List;

public interface ResourceStore {
	List<Resource> resources();

	void add(Resource resource);

	static ResourceStore collector() {
		return new ResourceStore() {
			List<Resource> resources = new ArrayList<>();
			@Override
			public List<Resource> resources() {
				return resources;
			}

			@Override
			public void add(Resource resource) {
				resources.add(resource);
			}
		};
	}
}
