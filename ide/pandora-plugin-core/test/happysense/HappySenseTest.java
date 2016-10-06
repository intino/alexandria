package happysense;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import io.intino.pandora.plugin.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.pandora.plugin.rest.RESTService;
import org.junit.Test;
import org.siani.pandora.server.PandoraSpark;
import tara.magritte.Graph;

import java.io.File;

public class HappySenseTest {

    private static final String HAPPYSENSE = "happysense";

    public static void main(String[] args) throws Exception {
        PandoraSpark pandoraSpark = new PandoraSpark(8080);
//		RestResources.setup(pandoraSpark, null);
    }

    @Test
    public void testCreation() {
        Graph happysense = Graph.load("Happysense").wrap(PandoraApplication.class);
        File gen = new File("test-gen", HAPPYSENSE);
        new FullRenderer(happysense, gen, gen, HAPPYSENSE).execute();
        happysense.find(RESTService.class).forEach(a -> new RESTAccessorRenderer(a, new File("test-gen/happysense"), HAPPYSENSE).execute());
    }
}
