package teseo;

import tara.magritte.Graph;

public class TeseoApplication extends GraphWrapper implements tara.magritte.Application {

	public TeseoApplication(Graph graph) {
		super(graph);
	}

	@Override
	public void execute(String... args) {
	}
}