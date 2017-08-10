package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.DataLake;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.utils.GraphLoader.loadGraph;
import static java.util.Arrays.asList;

public class PackageParameterUpdater {

	private Map<String, List<String>> staticParams = new HashMap<>();
	private Module module;
	private KonosGraph graph;

	public PackageParameterUpdater(Module module) {
		this.module = module;
		this.graph = loadGraph(module);
		staticParams.put("activity", asList("port", "webDirectory"));
		staticParams.put("datalake", asList("workingDirectory", "nessieToken"));
		staticParams.put("rest", asList("port", "webDirectory"));
		staticParams.put("jms", asList("url", "user", "password"));
		staticParams.put("slack", asList("token"));
		staticParams.put("jmx", asList("port"));
	}

	public void execute() {
		List<String> parameters = new ArrayList<>();
		for (Service service : graph.serviceList()) {
			if (service.isREST()) {
				parameters.addAll(wrap(service.name$(), this.staticParams.get("rest")));
				parameters.addAll(wrap(service.name$(), graph.findCustomParameters(service.asREST())));
			} else if (service.isJMS()) {
				parameters.addAll(wrap(service.name$(), this.staticParams.get("jms")));
				parameters.addAll(wrap(service.name$(), graph.findCustomParameters(service.asJMS())));
			}
		}
		if (graph.dataLake() != null) {
			parameters.addAll(wrap(graph.dataLake().name$(), staticParams.get("datalake")));
			for (DataLake.Tank tank : graph.dataLake().tankList())
				parameters.addAll(wrap(graph.dataLake().name$(), graph.findCustomParameters(tank)));
		}
		for (Activity activity : graph.activityList()) {
			parameters.addAll(wrap(activity.name$(), staticParams.get("activity")));
			parameters.addAll(wrap(activity.name$(), graph.findCustomParameters(activity)));
		}
		addParameterIfNotExist(parameters);
	}

	private List<String> wrap(String name, Collection<String> parameters) {
		return parameters.stream().map(p -> name + "_" + p).collect(Collectors.toList());
	}

	private void addParameterIfNotExist(List<String> parameters) {
		Configuration configuration = TaraUtil.configurationOf(module);
		for (String parameter : parameters) {

		}
	}
}
