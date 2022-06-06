package io.intino.konos.model.functions;

import io.intino.konos.model.Service;

import java.util.List;

@FunctionalInterface
public interface HomesProvider {

	List<Service.UI.Resource> homeList();
}
