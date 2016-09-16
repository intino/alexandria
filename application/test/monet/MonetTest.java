package monet;

import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.FullRenderer;

import java.io.File;

public class MonetTest {

	private static final String MONET = "monet";

	public static void main(String[] args) {
		final File gen = new File("test-gen", MONET);
		new FullRenderer(Graph.load("Monet").wrap(TeseoApplication.class), gen, gen, MONET).execute();
	}
}
