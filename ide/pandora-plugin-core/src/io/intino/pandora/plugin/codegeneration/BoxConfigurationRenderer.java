package io.intino.pandora.plugin.codegeneration;

import tara.magritte.Graph;

import java.io.File;

public class BoxConfigurationRenderer {

	private final Graph graph;
	private final File gen;
	private final File src;
	private final String packageName;

	public BoxConfigurationRenderer(Graph graph, File src, File gen, String packageName) {
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
	}

	public void execute() {

	}

}
