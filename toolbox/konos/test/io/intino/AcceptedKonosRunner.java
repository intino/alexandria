package io.intino;

import io.intino.alexandria.logger.Logger;
import io.intino.konos.KonoscRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class AcceptedKonosRunner {
	private String home;

	@Before
	public void setUp() {
		home = new File("test-res").getAbsolutePath() + File.separator;
	}

	@Test
	public void timbrado() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/cfe/timbrado.txt")});
	}

	@Test
	public void ui_doc() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/doc.txt")});
	}

	@Test
	public void ui_proxies() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/ui-proxies.txt")});
	}

	@Test
	public void unit() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/unit.txt")});
	}

	@Test
	public void layouts() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/layouts.txt")});
	}

	@Test
	public void ui() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/ui.txt")});
	}

	@Test
	public void uiWeb() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/ui-web.txt")});
	}

	@Test
	public void cli() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/cli.txt")});
	}

	@Test
	public void mobile() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/mobile.txt")});
	}

	@Test
	public void mobileExample() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/mobile-example.txt")});
	}

	@Test
	public void analytics() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/analytic.txt")});
	}

	@Test
	public void testAccessors() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/accessors.txt")});
	}

	@Test
	public void analyticBuilder() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/analytic_builder.txt")});
	}

	@Test
	public void test2() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/ideaKonosToCompile2.txt")});
	}

	@Test
	public void cuentamaestra() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/cfe/cuentamaestra.txt")});
	}

	@Test
	public void procurador() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/cfe/procurador.txt")});
	}

	@Test
	public void sumus() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/sumus.txt")});
	}

	@Test
	public void cesarDh() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/cesar/cesar-dh.txt")});
	}

	@Test
	public void cesar() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/cesar/cesar.txt")});
	}

	@Test
	public void amidas() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/amidas/amidas.txt")});
	}

	@Test
	public void serviciosSociales() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/servicios-sociales.txt")});
	}

	@Test
	public void limpiezaColegios() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/limpieza-colegios.txt")});
	}

	@Test
	public void fuentesOrnamentales() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/fuentes-ornamentales.txt")});
	}

	@Test
	public void cinepolis() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/cinepolis.txt")});
	}

	@Test
	public void sinergia() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/sinergia.full.txt")});
	}

	@Test
	public void accuee() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/accuee.txt")});
	}

	@Test
	public void tables() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/tables/ideaKonosToCompile.txt")});
	}

	@Test
	public void amidasPlatform() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/amidas/platform.txt")});
	}

	@Test
	public void workflow() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/workflow.txt")});
	}

	@Test
	public void temporal() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/temporal/ideaKonosToCompile.txt")});
	}

	@Test
	public void temp() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/temp/ideaKonosToCompile1.txt")});
	}

	@Test
	public void goros() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/ideaKonosToCompile2.txt")});
	}

	private static String temp(String filepath) {
		try {
			File file = new File(filepath);
			String home = System.getProperty("user.home");
			String text = Files.readString(file.toPath()).replace("$WORKSPACE", configurationWorkspace(home)).replace("$HOME", home);
			Path temporalFile = Files.createTempFile(file.getName(), ".txt");
			Files.writeString(temporalFile, text, StandardOpenOption.TRUNCATE_EXISTING);
			return temporalFile.toFile().getAbsolutePath();
		} catch (IOException e) {
			return null;
		}
	}

	private static String configurationWorkspace(String home) {
		try {
			InputStream stream = AcceptedKonosRunner.class.getResourceAsStream("/confFiles/workspace.txt");
			if (stream == null) return home;
			return new String(stream.readAllBytes());
		} catch (IOException e) {
			Logger.error(e);
			return home;
		}
	}
}
