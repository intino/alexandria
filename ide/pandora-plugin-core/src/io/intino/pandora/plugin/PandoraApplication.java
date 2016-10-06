package io.intino.pandora.plugin;

import tara.magritte.Graph;

public class PandoraApplication extends GraphWrapper implements tara.magritte.Application {

	public PandoraApplication(Graph graph) {
		super(graph);
	}

	@Override
	public void execute(String... args) {
	}

	@Override
	protected void update() {

	}
}