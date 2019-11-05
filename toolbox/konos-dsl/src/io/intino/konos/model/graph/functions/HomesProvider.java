package io.intino.konos.model.graph.functions;

import io.intino.konos.model.graph.Service;

import java.util.List;

@FunctionalInterface
public interface HomesProvider {

	List<Service.UI.Resource> homeList();
}
