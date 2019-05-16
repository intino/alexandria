package ui;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.ServiceRenderer;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UIGenerationTest {

	private static final String UI = "ui";
	private static final String DIALOG = "dialog";
	private static final String konos = "testkonos";
	private static final String ebar = "testebar";
	private static final String octana = "octana";
	private static final String marcet = "marcet";
	private static final String octanaPackage = "io.intino.octana.box";
	private static final String marcetPackage = "org.marcet.box";
	private static final String cesarPackage = "io.intino.cesar.ui";
	private static final String editorPackage = "io.intino.editor.box";
	private static final String asemedPackage = "io.intino.asemed.box";
	private static final String passiveViewPackage = "io.intino.passiveview.box";
	private static final String componentsPackage = "io.intino.components.box";
	private static final String DIR = "test-gen";


	@Test
	public void testUiAndDisplays() throws Exception {
		File gen = new File(DIR, UI);
		KonosGraph graph = new Graph().loadStashes("ui").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, UI).execute();
		for (UIService service : graph.uIServiceList()) new ServiceRenderer(new Settings().src(gen).gen(gen).cache(new ElementCache()), service).execute();
	}

	@Test
	public void testDialog() throws Exception {
		File gen = new File(DIR, DIALOG);
		KonosGraph graph = new Graph().loadStashes(DIALOG).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, DIALOG).execute();
	}

	@Test
	public void testKonos() throws Exception {
		execute(new File(DIR, konos), konos, konos);
	}

	@Test
	public void testEbar() throws Exception {
		execute(new File(DIR, ebar), ebar, ebar);
	}

	@Test
	public void testOctana() throws Exception {
		execute(new File(DIR, octanaPackage.replace(".", File.separator)), octana, octanaPackage);
	}

	@Test
	public void testMarcet() throws Exception {
		execute(new File(DIR, marcetPackage.replace(".", File.separator)), marcet, marcetPackage);
	}

	@Test
	public void testCesar() throws Exception {
		execute(new File(DIR, cesarPackage.replace(".", File.separator)), "testCesar", cesarPackage);
	}

	@Test
	public void testEditor() throws Exception {
		execute(new File(DIR, editorPackage.replace(".", File.separator)), "editor", editorPackage);
	}

	@Test
	public void testPassiveView() throws Exception {
		execute(new File(DIR, passiveViewPackage.replace(".", File.separator)), "passiveview", passiveViewPackage);
	}

	@Test
	public void testComponents() throws Exception {
		execute(new File(DIR, componentsPackage.replace(".", File.separator)), "components", componentsPackage);
	}

	@Test
	public void testAsemed() throws Exception {
		execute(new File(DIR, asemedPackage.replace(".", File.separator)), "asemed", asemedPackage);
	}

	private void execute(File gen, String stash, String workingPackage) {
		cleanTestDirectory();
		gen.mkdirs();
		KonosGraph graph = new Graph().loadStashes(stash).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, workingPackage.toLowerCase()).execute();
	}

	private void cleanTestDirectory() {
//		File directory = new File(DIR);
//		if (directory.exists()) Files.removeDir(directory);
	}

	private Module webModule() {
		Module mock = mock(Module.class);
		when(mock.getModuleFilePath()).thenReturn(new File(DIR).getPath() + "/web/parent");
		return mock;
	}

}
