package io.intino.konos.builder.codegeneration;

import io.intino.legio.graph.Parameter;
import io.intino.plugin.project.LegioConfiguration;

import java.util.List;
import java.util.Set;

import static io.intino.plugin.project.Safe.safeList;


public class ParameterPublisher {
	private LegioConfiguration configuration;

	public ParameterPublisher(LegioConfiguration configuration) {
		this.configuration = configuration;
	}

	public void publish(Set<String> customParameters) {
		final List<Parameter> parameters = safeList(() -> configuration.graph().artifact().parameterList());
		if (configuration != null)
			configuration.addParameters(customParameters.stream().filter(p -> parameters.stream().noneMatch(param -> param.name().equals(p))).toArray(String[]::new));
	}
}
