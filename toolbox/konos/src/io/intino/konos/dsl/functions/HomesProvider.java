package io.intino.konos.dsl.functions;

import io.intino.konos.dsl.Service;

import java.util.List;

@FunctionalInterface
public interface HomesProvider {

	List<Service.UI.Resource> homeList();
}
