package io.intino.alexandria.eventstore.graph;

import io.intino.alexandria.eventstore.Scale;
import io.intino.alexandria.eventstore.graph.Tank;
import io.intino.tara.magritte.Graph;

import java.io.File;

public class DatalakeGraph extends io.intino.alexandria.eventstore.graph.AbstractGraph {

	private File datalakeDirectory;
	private Scale scale;

	public DatalakeGraph(Graph graph) {
		super(graph);
	}

	public DatalakeGraph(Graph graph, io.intino.alexandria.eventstore.graph.DatalakeGraph wrapper) {
		super(graph, wrapper);
	}

	public Tank tank(String name) {
		return tankList(t -> t.qualifiedName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public Tank add(String tank) {
		if (tank(tank) != null) return tank(tank);
		final Tank newTank = this.create("tanks").tank(tank.replace(".", "-")).qualifiedName(tank).init();
		newTank.save$();
		return newTank;
	}

	public void remove(Tank tank) {
		tank.delete$();
	}

	public void rename(Tank tank, String name) {
		tank.rename(name);
	}

	public File directory() {
		return datalakeDirectory;
	}

	public io.intino.alexandria.eventstore.graph.DatalakeGraph directory(File datalakeDirectory) {
		this.datalakeDirectory = datalakeDirectory;
		datalakeDirectory.mkdirs();
		tankList().forEach(Tank::init);
		return this;
	}

	public Scale scale() {
		return scale;
	}

	public io.intino.alexandria.eventstore.graph.DatalakeGraph scale(Scale scale) {
		this.scale = scale;
		return this;
	}

}