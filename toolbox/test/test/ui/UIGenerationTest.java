package ui;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.ServiceRenderer;
import io.intino.konos.builder.codegeneration.cache.CacheReader;
import io.intino.konos.builder.codegeneration.cache.CacheWriter;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.tara.magritte.Graph;
import org.junit.Test;
import utils.TestUtil;

import java.io.File;

public class UIGenerationTest {

	private static final String UI = "components";
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

	private static final boolean IgnoreCache = true;


	@Test
	public void testCobranzaUi() throws Exception {
		File gen = new File(TestUtil.DIR, "cobranza");
		KonosGraph graph = new Graph().loadStashes("Cobranza").as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, "cobranza", loadCache(gen, graph))).execute();
		//for (Service.UI service : graph.uiServiceList()) new ServiceRenderer(new Settings().src(gen).gen(gen).cache(loadCache(gen, graph)), service).execute();
	}

	@Test
	public void testUiAndDisplays() throws Exception {
		File gen = new File(TestUtil.DIR, UI);
		KonosGraph graph = new Graph().loadStashes(UI).as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, UI, loadCache(gen, graph))).execute();
		for (Service.UI service : graph.uiServiceList()) new ServiceRenderer(new Settings().src(gen).gen(gen).cache(loadCache(gen, graph)), service).execute();
	}

	@Test
	public void testDialog() throws Exception {
		File gen = new File(TestUtil.DIR, DIALOG);
		KonosGraph graph = new Graph().loadStashes(DIALOG).as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, DIALOG, loadCache(gen, graph))).execute();
	}

	@Test
	public void testKonos() throws Exception {
		execute(new File(TestUtil.DIR, konos), konos, konos);
	}

	@Test
	public void testEbar() throws Exception {
		execute(new File(TestUtil.DIR, ebar), ebar, ebar);
	}

	@Test
	public void testOctana() throws Exception {
		execute(new File(TestUtil.DIR, octanaPackage.replace(".", File.separator)), octanaPackage, octana);
	}

	@Test
	public void testMarcet() throws Exception {
		execute(new File(TestUtil.DIR, marcetPackage.replace(".", File.separator)), marcetPackage, marcet);
	}

	@Test
	public void testCesar() throws Exception {
		execute(new File(TestUtil.DIR, cesarPackage.replace(".", File.separator)), cesarPackage, "testCesar");
	}

	@Test
	public void testEditor() throws Exception {
		execute(new File(TestUtil.DIR, editorPackage.replace(".", File.separator)), editorPackage, "editor");
	}

	@Test
	public void testPassiveView() throws Exception {
		execute(new File(TestUtil.DIR, passiveViewPackage.replace(".", File.separator)), passiveViewPackage, "passiveview");
	}

	@Test
	public void testComponents() throws Exception {
		String[] stashes = new String[] { "components2" };
		execute(new File(TestUtil.DIR, componentsPackage.replace(".", File.separator)), componentsPackage, stashes);
//		execute(new File(TestUtil.DIR, componentsPackage.replace(".", File.separator)), "components", componentsPackage);
	}

	@Test
	public void testAsemed() throws Exception {
		execute(new File(TestUtil.DIR, asemedPackage.replace(".", File.separator)), asemedPackage, "asemed");
	}

	private void execute(File gen, String workingPackage, String... stashes) {
		KonosGraph graph = new Graph().loadStashes(stashes).as(KonosGraph.class).init("test");
		ElementCache cache = loadCache(gen, graph);
		Settings settings = TestUtil.settings(gen, workingPackage.toLowerCase(), cache);
		cleanTestDirectory();
		gen.mkdirs();
		new FullRenderer(graph, TestUtil.settings(gen, workingPackage.toLowerCase(), cache)).execute();
		for (Service.UI service : graph.uiServiceList()) new ServiceRenderer(settings, service).execute();
		new CacheWriter(gen).save(cache);
	}

	private ElementCache loadCache(File folder, KonosGraph graph) {
		if (IgnoreCache) {
			File checksumFile = new File(folder, ".checksum");
			File cacheFile = new File(folder, ".cache");
			checksumFile.delete();
			cacheFile.delete();
		}
		return new CacheReader(folder).load(graph);
	}

	private void cleanTestDirectory() {
//		File directory = new File(TestUtil.DIR);
//		if (directory.exists()) Files.removeDir(directory);
	}

}
