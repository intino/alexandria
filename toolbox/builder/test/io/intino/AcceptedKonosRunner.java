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
	public void m1() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/konos/m1.txt")});
	}

	@Test
	public void cuentamaestra() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/cfe/cuentamaestra.txt")});
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
		KonoscRunner.main(new String[]{temp(home + "confFiles/amidas/amidas-team.txt")});
	}

	@Test
	public void pacma() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/pacma.txt")});
	}

	@Test
	public void temporal() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/temporal/ideaKonosToCompile.txt")});
	}

	@Test
	public void temp() {
		KonoscRunner.main(new String[]{temp(home + "confFiles/temporal/ideaKonosToCompile.txt")});
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
