package io.intino.alexandria;

import java.util.ArrayList;
import java.util.List;

public interface ResourceStore {
	List<Resource> resources();

	Resource get(String id);

	void put(Resource resource);

	class Memory implements ResourceStore {
		List<Resource> resources = new ArrayList<>();

		@Override
		public List<Resource> resources() {
			return resources;
		}

		@Override
		public Resource get(String id) {
			return resources.stream().filter(r -> r.id().equals(id)).findFirst().orElse(null);
		}

		@Override
		public void put(Resource resource) {
			resources.add(resource);
		}
	}
}