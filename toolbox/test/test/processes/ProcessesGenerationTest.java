package processes;

import io.intino.konos.builder.codegeneration.FullRenderer;
import org.junit.Test;

import java.io.File;

import static utils.TestUtil.graph;
import static utils.TestUtil.settings;

public class ProcessesGenerationTest {

	private static final String PROCESSES = "processes";

	@Test
	public void testProcesses() {
		new FullRenderer(graph(PROCESSES), settings(new File("test-gen", PROCESSES), PROCESSES)).execute();
	}
}