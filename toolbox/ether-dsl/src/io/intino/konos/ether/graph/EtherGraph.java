package io.intino.konos.ether.graph;

import io.intino.tara.magritte.Graph;

public class EtherGraph extends io.intino.konos.ether.graph.AbstractGraph {

	public EtherGraph(Graph graph) {
		super(graph);
	}

	public EtherGraph(io.intino.tara.magritte.Graph graph, EtherGraph wrapper) {
	    super(graph, wrapper);
	}
}